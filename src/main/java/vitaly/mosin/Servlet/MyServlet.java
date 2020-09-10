package vitaly.mosin.Servlet;

import vitaly.mosin.InitialDB;
import vitaly.mosin.entity.Share;
import vitaly.mosin.repository.CrudRepository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class MyServlet extends HttpServlet {
    private static final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("persistence-unit-shares");
    private static final EntityManagerFactory changeEntityManager =
            Persistence.createEntityManagerFactory("persistence-unit-changes");

    @Override
    public void init() throws ServletException {
        CrudRepository crudRepo = new CrudRepository(entityManagerFactory, changeEntityManager);
        Optional<List<Share>> result = crudRepo.findAll();
        if (result.isPresent()) {
            InitialDB.initDB();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }
}
