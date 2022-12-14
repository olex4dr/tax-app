package com.panchuk.taxapp.util;

import com.panchuk.taxapp.DAOException;
import com.panchuk.taxapp.constant.ProjectConstant;
import com.panchuk.taxapp.dao.DAOFactory;
import com.panchuk.taxapp.model.TaxType;
import javafx.scene.control.Alert;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private static DAOFactory daoFactory;

    static final Logger logger = Logger.getLogger(Validator.class);

    static {
        try {
            DAOFactory.setDAOFactoryFQN(ProjectConstant.DAO_FACTORY_FQN);
            daoFactory = DAOFactory.getInstance();
        } catch (Exception e) {
            System.out.println("EEE  " + e );
            LoggerController.daoInstantiatingException(e, Validator.class);
        }
    }

    public static boolean validationIdNumber(Integer inputData, Pattern pattern) {
        Matcher matcher = pattern.matcher(String.valueOf(inputData));
        if (!matcher.matches()) {
            System.out.println("\u26D4 It is not id number!!!");
            return false;
        }

        if (inputData <= 0) {
            System.out.println("\u26D4 User id must be positive!!!");
        }

        try {
            if (daoFactory.getTaxDAO().findUserByFilter(ProjectConstant.FIND_BY_ID + inputData).size() == 1) {
                logger.info("Validation Id Number - Correct");
                return true;
            }
        } catch (DAOException e) {
            logger.error(e);
            System.out.println("\u26D4 Validation Id Number Failed!");
        }
        return false;
    }

    public static boolean validationIdNumberForTax(int idPayment) {
        try {
            if (idPayment <= 0)  return false;
            List<TaxType> taxList = daoFactory.getTaxDAO().findTaxByFilter(ProjectConstant.FIND_TAX_BY_ID_PAYMENT + idPayment);

            if (taxList == null || taxList.size() == 0) {
                logger.info("Validation Id Number For Tax - Correct");
                return true;
            }
        } catch (DAOException e) {
            logger.error(e);
            System.out.println("\u26D4 Validation Id Number For Tax Failed!");
        }
        return false;
    }


    public static boolean validationString(String inputData, Pattern pattern) {
        return pattern.matcher(inputData).matches();
    }

    public static boolean isInteger(String strInt, String fieldName) {
        try {
            int i = Integer.parseInt(strInt);
            return true;
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.getDialogPane().setHeaderText("INPUT ERROR");
            alert.getDialogPane().setContentText("IT IS NOT INTEGER VALUE WAS ENTERED IN " + fieldName.toUpperCase() + " FIELD!");
            alert.showAndWait();
            return false;
        }
    }

    public static boolean isDouble(String strIDouble, String fieldName) {
        try {
            double d = Double.parseDouble(strIDouble);
            return true;
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.getDialogPane().setHeaderText("INPUT ERROR");
            alert.getDialogPane().setContentText("IT IS NOT DOUBLE VALUE WAS ENTERED AT " + fieldName.toUpperCase() + " FIELD!");
            alert.showAndWait();
            return false;
        }
    }
}
