import java.awt.EventQueue;
public class StartClient 
{
	public static void main(String[] args)
	 {
	        /*先新建一个jframe，然后把自动生成的代码贴过来*/
	    EventQueue.invokeLater(new Runnable() 
	    {
	        public void run() 
	        {
	            try 
	            {
	                MainWindow frame = new MainWindow();
	                frame.setVisible(true);
	                //创建完这个frame以后，传一份window的引用到ChatManager去
	                ChatManager.getChatManager().setWindow(frame);
	            } 
	            catch (Exception e) 
	            {
	                e.printStackTrace();
	            }
	        }
	    });
	 }
}
