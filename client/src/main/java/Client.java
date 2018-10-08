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
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        // получаем значение IP и PORT из файла settings.ini
        Properties props = new Properties();
        props.load(new FileInputStream(new File("client/src/main/resources/settings.ini")));
        String ip = props.getProperty("IP");
        String port = props.getProperty("PORT");

        // выводим сообщения в консоль, получаем объекты класса Request
        Scanner in = new Scanner(System.in);
        Request message = new Request();
        System.out.print("Ввведите имя сервера: " );
        String serverName = in.nextLine();
        System.out.print("Ввведите имя : " );
        message.setName(in.nextLine());
        System.out.print("Ввведите фамилию: " );
        message.setSurname(in.nextLine());
        System.out.print("Ввведите сообщеие: " );
        message.setMessage(in.nextLine());

        message.setDate(dateFormat.format(new Date(System.currentTimeMillis())));

        // преобразуем объект класса Request в xml файл (customer.xml)
        JAXBContext context = JAXBContext.newInstance(Request.class);
        File file = new File("customer.xml");
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(message, file);

        // открываем сокет и коннектимся
        Socket socket = new Socket(ip, Integer.parseInt(port));
        // определяем файл, переводим его в массив байтов
        // считываем байты из файла, накапливаем вводимые данные
        // создаём выходной поток сокета и передаём данные
        // заканчиваем передачу данных
        File transferFile = new File ("customer.xml");
        byte [] bytearray  = new byte[(int)transferFile.length()];
        FileInputStream fin = new FileInputStream(transferFile);
        BufferedInputStream bin = new BufferedInputStream(fin);
        bin.read(bytearray,0,bytearray.length);
        OutputStream os = socket.getOutputStream();
        System.out.println("Sending Files...");
        os.write(bytearray,0,bytearray.length);
        os.flush();
        os.close();
        System.out.println("File transfer complete");



    }
}
