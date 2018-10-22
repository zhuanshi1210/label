import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class ChatSocket extends Thread
{
	Socket socket;

    public ChatSocket(Socket s) 
    {
        this.socket = s;
    }
    //发送数据
    public void out(String out) 
    {
        try {
            socket.getOutputStream().write(out.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //服务器会不断地从客户端读取内容，把读取到的内容发给集合内所有的客户端。
    public void run() 
    {
        try {
            //接收数据
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream(), "UTF-8"));
            String line=null;
            //发送读到的内容
            while ((line = br.readLine())!=null) {
                System.out.println(line);
                ChatManager.getChatManager().publish(this, line);
            }
            br.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
