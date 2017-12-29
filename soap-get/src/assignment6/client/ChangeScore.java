package assignment6.client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ChangeScore {
    public static void main(String[]args){
        String urlString = "http://localhost:8081/modify";
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection httpConn = null;//打开连接
        try {
            httpConn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //String soapActionString = "http://localhost:9000/HelloWorld/sayHelloWorldFrom";//Soap 1.1中使用

        String xmlFile = new ChangeScore().getClass().getResource("changeInfo.xml").getPath();//要发送的soap格式文件
        File fileToSend = new File(xmlFile);
        byte[] buf = new byte[(int) fileToSend.length()];// 用于存放文件数据的数组

        try {
            new FileInputStream(xmlFile).read(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Content-Length长度会自动进行计算
        httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        //httpConn.setRequestProperty("soapActionString",soapActionString);//Soap1.1使用 其实完全可以不需要
        try {
            httpConn.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);

        OutputStream out = null;
        try {
            out = httpConn.getOutputStream();
            out.write(buf);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStreamReader is = new InputStreamReader(httpConn.getInputStream());
                BufferedReader in = new BufferedReader(is);
                String inputLine;
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream("result.xml")));// 将结果存放的位置
                while ((inputLine = in.readLine()) != null)
                {
                    System.out.println(inputLine);
                    bw.write(inputLine);
                    bw.newLine();
                }
                bw.close();
                in.close();
            }
            else{
                //如果服务器返回的HTTP状态不是HTTP_OK，则表示发生了错误，此时可以通过如下方法了解错误原因。
                InputStream is = httpConn.getErrorStream();    //通过getErrorStream了解错误的详情，因为错误详情也以XML格式返回，因此也可以用JDOM来获取。
                InputStreamReader isr = new InputStreamReader(is,"utf-8");
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream("result.xml")));// 将结果存放的位置
                while ((inputLine = in.readLine()) != null)
                {
                    System.out.println(inputLine);
                    bw.write(inputLine);
                    bw.newLine();
                    bw.close();
                }
                in.close();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpConn.disconnect();
    }

}
