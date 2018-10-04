import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException, JAXBException {
        Properties props = new Properties();
        props.load(new FileInputStream(new File("client\\src\\main\\resources\\settings.ini")));
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

        String ip = props.getProperty("IP");
        String port = props.getProperty("PORT");


        Scanner in = new Scanner(System.in);
        User message=new User();
        System.out.print("Ввведите имя сервера: " );
        String serverName = in.nextLine();
        System.out.print("Ввведите имя : " );
        message.setName(in.nextLine());
        System.out.print("Ввведите фамилию: " );
        message.setSurname(in.nextLine());
        System.out.print("Ввведите сообщеие: " );
        message.setMessage(in.nextLine());

        message.setDate(dateFormat.format(new Date(System.currentTimeMillis())));

        JAXBContext context = JAXBContext.newInstance(User.class);
        File file = new File("customer.xml");
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(message, file);

        try (Socket socket = new Socket(ip, Integer.parseInt(port))) {
            try (PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {



                toServer.println("Hello from TEST");
                // Ответ сервера

                String line = fromServer.readLine();
                System.out.println(line);
            }
        }


    }
}
