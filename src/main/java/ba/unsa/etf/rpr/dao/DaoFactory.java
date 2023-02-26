package ba.unsa.etf.rpr.dao;

/**
 * Factory method for singleton implementation of DAOs
 *
 * @author Amina Bajric
 */
public class DaoFactory {

    private static final BookDao bookDao = BookDaoSQLImpl.getInstance();
    private static final MemberDao memberDao = MemberDaoSQLImpl.getInstance();
    private static final RentalDao rentalDao = RentalDaoSQLImpl.getInstance();
    private DaoFactory(){
    }

    public static BookDao bookDao(){
        return bookDao;
    }

    public static MemberDao memberDao(){
        return memberDao;
    }

    public static RentalDao rentalDao(){
        return rentalDao;
    }

}
