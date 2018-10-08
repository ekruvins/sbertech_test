import org.apache.log4j.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {
    // Инициализация логера
    final static Logger logger = Logger.getLogger(Server.class);
    static SimpleDateFormat  dateFormat= new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

    public static void main(String[] args) throws IOException, JAXBException {
        try {
            int serverPort = 5000;
            // создаём сокет и привязываем его к порту
            ServerSocket serverSocket = new ServerSocket(serverPort);
            while (true) {
                Socket server = serverSocket.accept();
                File file = parseRequestXml(server);
                Request request = getRequsetFromFile(file);
                System.out.println(request.getMessage());
                Response response = generateResponseMessage(request);
                File responseFile = generateXMLResponse(response);

            }
        // обработка исключений
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            try {
                if (e instanceof SocketTimeoutException) {
                    throw new SocketTimeoutException();
                } else {
                    e.printStackTrace();
                }
            } catch (SocketTimeoutException ste) {
                System.out.println("Turn off the server by timeout");
            }
        }
    }

    private static Response generateResponseMessage(Request request) {
        // пишем необходимую информацию в логере
        // для объкта response класса Response вызываем методы
        logger.debug("User with name: " + request.getName() + " was register");
        logger.debug("User with name: " + request.getName() + " send message: " + request.getMessage());
        Response response = new Response();
        response.setMessage("Добрый день, " + request.getName() + ", Ваше сообщение успешно обработано!");
        response.setDate(dateFormat.format(new Date(System.currentTimeMillis())));
        return response;

    }
    private static File generateXMLResponse(Response response) throws JAXBException {
        // преобразование объект класса Response в xml файл(response.xml)
        JAXBContext context = JAXBContext.newInstance(Response.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        File responseFile = new File("response.xml");
        marshaller.marshal(response, responseFile);
        return responseFile;
    }

    private static Request getRequsetFromFile(File file) throws JAXBException {
        // преобразуем файл в объект класса Request
        JAXBContext jc = JAXBContext.newInstance(Request.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        return (Request) unmarshaller.unmarshal(file);

    }

    private static File parseRequestXml(Socket server) throws IOException {
        // берём поток входяших данных
        // создаём буффер данных, определяем файл, записываем байты в файл
        // читаем пока не встретим -1(конец потока)
        // выводим данные, заканчиваем передачу, закрываем соединение
        InputStream is = server.getInputStream();
        byte[] bytearray = new byte[8000];
        File copyFileName = new File("output.xml");
        FileOutputStream fos = new FileOutputStream(copyFileName);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        int bytesRead = is.read(bytearray, 0, bytearray.length);
        int currentTot = bytesRead;
        do {
            bytesRead = is.read(bytearray, currentTot, (bytearray.length - currentTot));
            if (bytesRead >= 0)
                currentTot += bytesRead;
        } while (bytesRead > -1);
        bos.write(bytearray, 0, currentTot);
        bos.flush();
        bos.close();
        return copyFileName;

    }

}


