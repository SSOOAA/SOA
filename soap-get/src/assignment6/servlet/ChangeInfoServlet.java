package assignment6.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/change")
public class ChangeInfoServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter out = resp.getWriter();

        out.println("<html><body>");
        out.println("<h2>修改成绩</h2>");
        out.println("<form method='POST' action='"+req.getContextPath()+"/modify'>");
        out.println("<span>学号:</span><input name='sid' type='text' /><br /><br />");
        out.println("<span>课程编号</span><input name='cid' type='text' /><br /><br />");
        out.println("<span>成绩性质：</span><input name='sType' type='text' /><br /><br />");
        out.println("<span>成绩:</span><input name='score' type='text' /><br /><br />");
        out.println("<input type='submit' name='提交' />");
        out.println("</form></body></html>");
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
