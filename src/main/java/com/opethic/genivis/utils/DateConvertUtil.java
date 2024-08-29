package com.opethic.genivis.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateConvertUtil {
    public static LocalDate convertDateToLocalDate(Date date) {
        return LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(date));
    }

    public static LocalDate convertStringToLocalDate(String date) {
        return LocalDate.parse(date);
//        return LocalDate.parse(new SimpleDateFormat("YYYY-MM-dd").format(date));
    }

    public static Date convertStringToDate(String date) {
        Date dt = new Date();
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String str[] = sdf1.format(dt).split(" ");
            String time = str[1];
            date = date + " " + time;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            dt = sdf.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return dt;
    }

    public static String convertLocalDatetoString(LocalDate inDate) {
        String pattern = "dd/MM/yyyy"; // Define your desired date pattern
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
        return inDate != null ? dateFormatter.format(inDate) : "";

    }


    public static String convertDatetoAPIDateString(LocalDate inDate) {
        String pattern = "yyyy-MM-dd"; // Define your desired date pattern
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
        return inDate != null ? dateFormatter.format(inDate) : "";
    }

    public static String convertDispDateFormat(String inputDate) {
        String inPattern = "yyyy-MM-dd"; // Define your desired date pattern
        String outPattern = "dd/MM/yyyy";
        // Create SimpleDateFormat objects for input and output formats
        SimpleDateFormat inputFormat = new SimpleDateFormat(inPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outPattern);
        String outputDate = "";
        try {
            if (!inputDate.isEmpty()) {
                // Parse the input date using inputFormat
                Date date = inputFormat.parse(inputDate);
                // Format the date using outputFormat
                outputDate = outputFormat.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputDate;
    }


    public static String convertStringDatetoAPIDateString(String inputDate) {
        String inPattern = "dd/MM/yyyy";
        String outPattern = "yyyy-MM-dd"; // Define your desired date pattern
        // Create SimpleDateFormat objects for input and output formats
        SimpleDateFormat inputFormat = new SimpleDateFormat(inPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outPattern);
        String outputDate = "";
        try {
            if (!inputDate.isEmpty()) {
                // Parse the input date using inputFormat
                Date date = inputFormat.parse(inputDate);
                // Format the date using outputFormat
                outputDate = outputFormat.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputDate;
    }

    public static LocalDate convertStringToLocalDateWithFormat(String inputDate) {

        String inPattern = "yyyy-MM-dd HH:mm:ss.SSS"; // Define your desired date pattern
//        String outPattern = "dd/MM/yyyy";
        // Create SimpleDateFormat objects for input and output formats
        SimpleDateFormat inputFormat = new SimpleDateFormat(inPattern);
//        SimpleDateFormat outputFormat = new SimpleDateFormat(outPattern);
        String outputDate = "";
        LocalDate returnDate = null;
        try {
            if (!inputDate.isEmpty()) {
                // Parse the input date using inputFormat
                Date date = inputFormat.parse(inputDate);
                // Format the date using outputFormat
//                outputDate = outputFormat.format(date);
                returnDate = convertDateToLocalDate(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return outputDate;
        return returnDate;
    }

    public static LocalDate convertTfStringToLocalDate(String inputDate) {
        String inPattern = "dd/MM/yyyy";
//        SimpleDateFormat inputFormat = new SimpleDateFormat(inPattern);Date date =null;
//
//        try {
//            if (!inputDate.isEmpty()) {
//                // Parse the input date using inputFormat
//                date=inputFormat.parse(inputDate);
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return LocalDate.parse(date);
        return LocalDate.parse(new SimpleDateFormat(inPattern).format(inputDate));
    }
}
