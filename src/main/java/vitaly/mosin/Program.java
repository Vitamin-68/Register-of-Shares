package vitaly.mosin;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import vitaly.mosin.entity.Share;
import vitaly.mosin.utils.ConnectionDB;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Program {
    public static void main(String[] args) {

        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection connShares = ConnectionDB.getConnect("shares");
        Connection connChanges = ConnectionDB.getConnect("changes");

        Share sh = new Share();


        Gson gs = new Gson();


        String strSql = "insert into SHARES (EDRPOU, quantity, price, cost, date, comment, status) " +
                "values (?,?,?,?,?,?,?)";
        try {
            Type listOfMyClassObject = new TypeToken<ArrayList<Share>>() {}.getType();
            List<Share> shareList = gs.fromJson(new FileReader("./src/main/resources/dev/Initial.json"), listOfMyClassObject);
            System.out.println(sh);
            System.out.println(shareList);
            for (Share share : shareList) {
                PreparedStatement prepStatement = connShares.prepareStatement(strSql);
                prepStatement.setInt(1, share.getEdrpou());
                prepStatement.setInt(2, share.getQuantity());
                prepStatement.setDouble(3, share.getPrice());
                prepStatement.setDouble(4, share.getCost());
                prepStatement.setDate(5, Date.valueOf(share.getDate()));
                prepStatement.setString(6, share.getComment());
                prepStatement.setBoolean(7, share.isStatus());
                prepStatement.execute();
            }
        } catch (FileNotFoundException | SQLException e) {
            e.printStackTrace();
        }


        ConnectionDB.closeConnection("shares");
        ConnectionDB.closeConnection("changes");


    }
}
