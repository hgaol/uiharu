package com.github.hgaol.uiharu;

import com.github.hgaol.uiharu.annotation.Body;
import com.github.hgaol.uiharu.bean.Data;
import com.github.hgaol.uiharu.bean.Handler;
import com.github.hgaol.uiharu.bean.Param;
import com.github.hgaol.uiharu.bean.View;
import com.github.hgaol.uiharu.helper.*;
import com.github.hgaol.uiharu.util.JsonUtils;
import com.github.hgaol.uiharu.util.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * 请求转发核心逻辑
 *
 * @author: gaohan
 * @date: 2018-08-22 15:15
 **/
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        // 加载ioc等类
        HelperLoader.init();

        ServletContext servletContext = config.getServletContext();

        registerServlet(servletContext);
    }

    private void registerServlet(ServletContext servletContext) {
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping("/index.jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");

        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping("/favicon.ico");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        ServletHelper.init(req, resp);
        try {
            String requestMethod = req.getMethod().toLowerCase();
            String requestPath = req.getPathInfo();
            Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
            if (handler == null) {
                // todo 返回默认404的页面
                ServletHelper.sendRedirect(req.getContextPath() + "/404.jsp");
                return;
            }

            // todo parse params
            Param param = RequestHelper.createRequestParams(req);
            String body = RequestHelper.createRequestBody(req);
            Object reqBody = null;

            // 反射调用request对应的方法
            Object controllerBean = BeanHelper.getBean(handler.getControllerClass());

            Object result;
            Method actionMethod = handler.getActionMethod();
            Parameter[] params = actionMethod.getParameters();
            for (Parameter parameter : params) {
                if (parameter.isAnnotationPresent(Body.class)) {
                    reqBody = JsonUtils.toPojo(body, parameter.getType());
                }
            }
//            if (param.isEmpty()) {
//                result = ReflectionUtils.invokeMethod(controllerBean, actionMethod);
//            } else {
                result = ReflectionUtils.invokeMethod(controllerBean, actionMethod, param, reqBody);
//            }

            if (result instanceof View) {
                handleViewResult((View) result, req, resp);
            } else if (result instanceof Data) {
                handleDataResult((Data) result, resp);
            }
        } finally {
            ServletHelper.destroy();
        }
    }

    /**
     * 返回json数据
     */
    private void handleDataResult(Data data, HttpServletResponse resp) throws IOException {
        Object model = data.getModel();
        if (model != null) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter writer = resp.getWriter();
            String json = JsonUtils.toString(model);
            writer.write(json);
            writer.flush();
            writer.close();
        }
    }

    private void handleViewResult(View view, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String path = view.getPath();
        if (StringUtils.isNotEmpty(path)) {
            if (path.startsWith("/")) {
                resp.sendRedirect(req.getContextPath() + path);
            } else {
                Map<String, Object> model = view.getModel();
                for (Map.Entry<String, Object> entry : model.entrySet()) {
                    req.setAttribute(entry.getKey(), entry.getValue());
                }
                req.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(req, resp);
            }
        }
    }

}
