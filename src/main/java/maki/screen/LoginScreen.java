package maki.screen;

import cpw.mods.fml.common.FMLCommonHandler;
import maki.utils.LoginUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginScreen {
    public static boolean kkkkkk;

    public static JFrame frame = new JFrame();
    public static JButton btn_login ;
    public static JTextField user ;
    public static JPasswordField pass;
    public LoginScreen() {
        btn_login = new JButton("Login");
        user = new JTextField();
        pass = new JPasswordField ();
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Login"));
        panel.setLayout(new GridLayout(5, -10));
        panel.add(new JLabel("Username:"));
        panel.add(user);
        panel.add(new JLabel("Password:"));
        panel.add(pass);
        panel.add(btn_login);
        frame.add(panel);

        frame.setSize(257, 213);
        int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
        frame.setTitle("Login");
        frame.setLocation((screen_width - frame.getWidth()) / 2, (screen_height - frame.getHeight()) / 2);
        frame.setResizable(false);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                FMLCommonHandler.instance().exitJava(0,true);
            }
        });
        btn_login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg) {
                String username = user.getText();
                String password=new String(pass.getPassword());
                if (username == null || username.equals("") || password == null || password.equals("")) {
                    return;
                }
                user.setEnabled(false);
                pass.setEnabled(false);
                btn_login.setEnabled(false);
                LoginUtil.doLogin(username, password);
            }
        });
        kkkkkk = false;
    }
}

