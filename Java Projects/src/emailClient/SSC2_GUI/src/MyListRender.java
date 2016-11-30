import java.awt.Color;
import java.awt.Component;


import javax.mail.MessagingException;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class MyListRender extends DefaultListCellRenderer  
    {  
        /**
	 * Colours the foreground of the Jlist dependent on the flag of the messages : RED unseen; BLACK seen
	 */
	private static final long serialVersionUID = 1L;
	
		public Component getListCellRendererComponent( JList<?> list,  
                Object value, int index, boolean isSelected,  
                boolean cellHasFocus )  
        {     setForeground( Color.black );  
			
            super.getListCellRendererComponent( list, value, index,  
                    isSelected, cellHasFocus );
   
         
            
            //if the mouse is selecting the J List it colours it in black otherwise into red
            try {
				setForeground(GUI.geKey(index) ? Color.black : Color.red);
			} catch (MessagingException e) {
				
			
			}
            
	
           
   
            return( this );  
        }  
    
    }  