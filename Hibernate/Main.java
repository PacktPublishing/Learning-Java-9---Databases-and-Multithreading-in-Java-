package Product4.Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.net.URL;
import java.util.List;

public class Main {
    private static SessionFactory factory;

    public static void main(String args[]) throws Exception {
        setUp();

        getCars();

        URL file_path = Main.class.getClassLoader().getResource("cars.json");
        JSONProcessor jsonProcessor = new JSONProcessor(file_path.getPath());
        List<Car> cars = jsonProcessor.parseFile();

        cars.forEach(Main::addCar);
        System.exit(0);
    }

    private static void setUp() {
        factory = new Configuration()
            .addAnnotatedClass(Car.class)
            .configure() // configures settings from hibernate.cfg.xml
            .buildSessionFactory();
    }

    private static Integer addCar(Car car) {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        Integer carId = (Integer) session.save(car);
        tx.commit();

        return carId;
    }

    private static List<Car> getCars() {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();

        List<Car> cars = session.createQuery("FROM Car").list();
        System.out.println();

        return null;
    }
}
