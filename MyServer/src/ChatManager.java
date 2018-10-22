import java.util.Vector;
public class ChatManager 
{
	private ChatManager(){}
    private static final ChatManager CM = new ChatManager();
    public static ChatManager getChatManager()
    {
        return CM;
    }
    Vector<ChatSocket> vector = new Vector<ChatSocket>();
    /*增加ChatSocket 实例到vector中*/
    public void add(ChatSocket cs)
    {
        vector.add(cs);
    }
    /*发布消息给其他客户端
     *ChatSocket cs： 调用publish的线程
     *msg：要发送的信息 */
    public void publish(ChatSocket cs, String msg)
    {
        for (int i = 0; i < vector.size(); i++) 
        {
            ChatSocket csTemp = vector.get(i);
            if (!cs.equals(csTemp)) 
            {
                csTemp.out(msg+"\n");//不用发送给自己。
            }
        }
    }
}
