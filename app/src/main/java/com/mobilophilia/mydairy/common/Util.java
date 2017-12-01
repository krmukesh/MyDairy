package com.mobilophilia.mydairy.common;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mobilophilia.mydairy.R;
import com.mobilophilia.mydairy.database.DBHelper;
import com.mobilophilia.mydairy.database.EnterNameEntry;
import com.mobilophilia.mydairy.database.MyEntries;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import java.io.FileOutputStream;
import java.text.DecimalFormat;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Created by yogen on 18-07-2017.
 */

public class Util {

    public static Dialog dialog;

    public static void launchBarDialog(Activity activity, String msg) {
        dismissBarDialog();
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progress_dialog);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        TextView text = (TextView) dialog.findViewById(R.id.dia_msg);
        text.setText(msg);
        dialog.show();
    }


    public static void launchMessageDialog(Activity activity, String msg) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_message);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        TextView text = (TextView) dialog.findViewById(R.id.dia_msg);
        text.setText(msg);

        Button btnOk = (Button) dialog.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public static void dismissBarDialog() {
        try {
            if(dialog!=null) {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isEmpty(String str) {
        return str == null || str.equalsIgnoreCase("null")
                || str.equalsIgnoreCase("") || str.length() < 1 || str.equalsIgnoreCase(" ");
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean isValidPhoneNumber(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            if (target.length() < 10) {
                return false;
            } else {
                return android.util.Patterns.PHONE.matcher(target).matches();
            }
        }
    }

    public static void validatioMessage(View view, String message) {
        Snackbar.make(view, "" + message, Snackbar.LENGTH_LONG).show();
    }


    public static Double convertIntoSNF(Double clr, Double fat) {
        return ((clr / 4) + (0.21 * fat) + 0.36);
    }


    public static String createAppFolder(String agentId) {
        String path = "";
        File dir;

        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyDairyApp/" + agentId;
        dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return path;
    }


    public static int getTime() {
        int shift = 0;
        Calendar calander = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("a");
        String time = simpleDateFormat.format(calander.getTime());
       /* SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        String time = simpleDateFormat.format(calander.getTime());*/
        if (time.equalsIgnoreCase("AM") || time.equalsIgnoreCase("a.m.")) {
            shift = 0;
        } else {
            shift = 1;
        }
        return shift;
    }

    public static Double getPrice(Double basePrice, Double inputFat, Double calSnf, Double fatInterval, Double snfInterval, Double lowFat, Double highFat, Double lowSnf, Double HighSnf) {


        int colomCount, rowCount;
        double ds = (HighSnf - lowSnf);
        double df = (highFat - lowFat);
        double iC = ds / 0.1;
        double jR = df / 0.1;
        colomCount = (int) iC + 2;
        rowCount = (int) jR + 2;

        Long fatIndex = Math.round(((inputFat - lowFat) * 10));
        Long snfIndex = Math.round(((calSnf - lowSnf) * 10));

        int fatR = (snfIndex.intValue()) + 2;
        int snfC = (fatIndex.intValue());

        double price = 0;
        double myprice = 0;
        for (int i = 0; i < rowCount; i++) {
            price = basePrice;
            for (int j = 0; j < colomCount; j++) {
                if (i == 0 && j == 0) {
                } else if (i == 0 && j > 0) {
                } else if (i > 0 && j == 0) {
                } else if (i == 1 && j == 1) {
                    price = price + snfInterval;
                } else {
                    price = price + snfInterval;
                }
                if ((i == snfC) && (j == fatR)) {
                    myprice = price;
                    break;
                }
            }
            if (i != 0) {
                basePrice = basePrice + fatInterval;
            }
        }

        return myprice;
    }

    public static void iSenableButton(Context con, Button button, boolean isEnable) {

        if (isEnable) {
            button.setEnabled(isEnable);
            button.setTextColor(con.getResources().getColor(R.color.con_theme));
            button.setBackground(con.getResources().getDrawable(R.drawable.border_blue));
        } else {
            button.setEnabled(isEnable);
            button.setTextColor(con.getResources().getColor(R.color.black_overlay));
            button.setBackground(con.getResources().getDrawable(R.drawable.border_gray));
        }
        //button.setBackground(con.getResources().getDrawable(R.drawable.border_blue));
    }

    public static void setBlankAndIsEnable(EditText editBOX, boolean isEnable) {
        if (editBOX.getText().toString().trim().length() > 0) {
            editBOX.setText("");
        }
        editBOX.setEnabled(isEnable);
    }

    public static void setEnable(EditText editBOX, boolean isEnable) {
        editBOX.setEnabled(isEnable);
    }

    public static void setTextBlank(TextView textView) {
        textView.setText("");
    }

    public static void setEnableTv(TextView textView, boolean isEnable) {
        textView.setEnabled(isEnable);
    }

    public static void setTVAndIsEnable(TextView textView, boolean isEnable) {
        if (isEnable) {
            textView.setEnabled(isEnable);
        } else {
            textView.setEnabled(isEnable);
        }
        textView.setText("");
    }


    public static double round0ffOnePlace(double value) {
        if (1 < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static double round0ffTwoPlace(double value) {
        if (2 < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    public static String getTypeFromSelected(int positionDropDown) {
        String type = "";
        if (positionDropDown == 0) {
            type = "Cow";
        } else if (positionDropDown == 1) {
            type = "Buffalo";
        }
        return type;
    }

    public static String capitalizeString(String string) {

        if (string == null || string.trim().isEmpty()) {
            return string;
        }
        char c[] = string.trim().toLowerCase().toCharArray();
        c[0] = Character.toUpperCase(c[0]);

        return new String(c);

    }


    public static void createPDF(String fileName, Context context, List<MyEntries> arrayMyEntries, String pathUrl, String[] info, boolean isDateType) {

        Document doc = new Document();
        PdfWriter docWriter = null;
        File dir = null;
        File file;

        try {
            //special font sizes
            Font bfBold12 = new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(FontFamily.TIMES_ROMAN, 12);
            Font bf13 = new Font(FontFamily.TIMES_ROMAN, 15, Font.NORMAL, new BaseColor(0, 0, 0));

            dir = new File(pathUrl);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            com.mobilophilia.mydairy.common.Log.e("PDFCreator", "PDF Path: " + pathUrl);
            SimpleDateFormat sdf = new SimpleDateFormat("MMyyyyhhmmss");
            file = new File(dir, fileName + "_" + sdf.format(Calendar.getInstance().getTime()) + ".pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            // PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            docWriter = PdfWriter.getInstance(doc, fOut);

            //document header attributes
            doc.addAuthor("Mahi");
            doc.addCreationDate();
            doc.addProducer();
            doc.addCreator("mobilophilia.com");
            doc.addTitle("" + fileName);
            doc.setPageSize(PageSize.LETTER);

            //open document
            doc.open();


            Paragraph paragraphLogo = new Paragraph("");

            //set Logo in cell
            Drawable myImage = context.getResources().getDrawable(R.drawable.ic_rsz_app_icon);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();
            Image bgImage = Image.getInstance(bitmapdata);
            bgImage.setAbsolutePosition(330f, 642f);

            PdfPCell cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.addElement(bgImage);

            PdfPTable tableLogo = new PdfPTable(1);
            tableLogo.setWidthPercentage(10f);
            tableLogo.addCell(cell);
            //End Of Logo

            //Report Name Start
            Paragraph paragraphName = new Paragraph("");
            paragraphName.setSpacingAfter(5f);
            paragraphName.setSpacingBefore(5f);
            paragraphName.add(new Phrase("" + fileName, bf13));
            paragraphName.setAlignment(Element.ALIGN_CENTER);
            //Report Name End

            Paragraph paragraphInfo = new Paragraph("");
            float[] columnWidthInfo = new float[]{30, 30, 30};
            PdfPTable tableInfo = new PdfPTable(columnWidthInfo);
            tableInfo.setWidthPercentage(90f);
            //insert column headings
            int count = arrayMyEntries.size();
            MyEntries me = arrayMyEntries.get((count - 1));
            Double fat = me.avgFat / count;
            Double snf = me.avgSnf / count;
            Double price = me.avgPrice / count;
            Double ltr = me.totalLtr;
            Double gtotal = me.grandPrice;

            insertCell(tableInfo, "Avg FAT: " + String.format("%.2f", fat), Element.ALIGN_CENTER, 1, bfBold12, false);
            insertCell(tableInfo, "Avg Price: " + String.format("%.2f", price), Element.ALIGN_CENTER, 1, bfBold12, false);
            if (isDateType) {
                insertCell(tableInfo, "Date: " + info[0], Element.ALIGN_CENTER, 1, bfBold12, false);
            } else {
                insertCell(tableInfo, "Code " + info[0] + "-" + info[1], Element.ALIGN_CENTER, 1, bfBold12, false);
            }

            insertCell(tableInfo, "Avg SNF: " + String.format("%.2f", snf), Element.ALIGN_CENTER, 1, bfBold12, false);
            insertCell(tableInfo, "Total Litre: " + String.format("%.1f", ltr), Element.ALIGN_CENTER, 1, bfBold12, false);
            if (isDateType) {
                insertCell(tableInfo, "", Element.ALIGN_CENTER, 1, bfBold12, false);
            } else {
                insertCell(tableInfo, "" + info[2] + " To " + info[3], Element.ALIGN_CENTER, 1, bfBold12, false);
            }


            insertCell(tableInfo, "", Element.ALIGN_CENTER, 1, bfBold12, false);
            insertCell(tableInfo, "Total: " + String.format("%.2f", gtotal), Element.ALIGN_CENTER, 1, bfBold12, false);
            insertCell(tableInfo, "", Element.ALIGN_CENTER, 1, bfBold12, false);


            Paragraph paragraphData = new Paragraph("");
            float[] columnWidths = new float[]{13, 20,50, 30, 30, 30, 30, 30, 30, 30};
            // float[] columnWidths = {1.5f, 2f, 5f, 2f};
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(90f);

            //insert column headings
            insertCell(table, "#", Element.ALIGN_CENTER, 1, bfBold12, true);
            insertCell(table, "Code", Element.ALIGN_CENTER, 1, bfBold12, true);
            insertCell(table, "Date", Element.ALIGN_CENTER, 1, bfBold12, true);
            insertCell(table, "Name", Element.ALIGN_CENTER, 1, bfBold12, true);
            insertCell(table, "Type", Element.ALIGN_CENTER, 1, bfBold12, true);
            insertCell(table, "FAT", Element.ALIGN_CENTER, 1, bfBold12, true);

            insertCell(table, "SNF", Element.ALIGN_CENTER, 1, bfBold12, true);
            insertCell(table, "Price", Element.ALIGN_CENTER, 1, bfBold12, true);
            insertCell(table, "Litre", Element.ALIGN_CENTER, 1, bfBold12, true);
            insertCell(table, "Total", Element.ALIGN_CENTER, 1, bfBold12, true);
            table.setHeaderRows(1);

            double orderTotal, total = 0;


            for (int i = 0; i < arrayMyEntries.size(); i++) {
                insertCell(table, "" + String.valueOf((i + 1)), Element.ALIGN_CENTER, 1, bf12, true);
                MyEntries entries = arrayMyEntries.get(i);

                insertCell(table, "" + entries.getCode(), Element.ALIGN_CENTER, 1, bf12, true);
                insertCell(table, "" + DBHelper.getDateTimeFromTimeStamp(entries.getCreatedAt()), Element.ALIGN_CENTER, 1, bf12, true);
                insertCell(table, "" + capitalizeString(entries.getName()), Element.ALIGN_CENTER, 1, bf12, true);
                insertCell(table, "" + Util.getTypeFromSelected(entries.getMilkType()), Element.ALIGN_CENTER, 1, bf12, true);
                insertCell(table, "" + entries.getFat(), Element.ALIGN_CENTER, 1, bf12, true);
                insertCell(table, "" + entries.getSnf(), Element.ALIGN_CENTER, 1, bf12, true);
                insertCell(table, "" + String.format("%.2f", entries.getPrice()), Element.ALIGN_CENTER, 1, bf12, true);
                insertCell(table, "" + entries.getLtr(), Element.ALIGN_CENTER, 1, bf12, true);
                insertCell(table, "" + String.format("%.2f", entries.getTotal()), Element.ALIGN_CENTER, 1, bf12, true);
            }

            Paragraph paragraphDate = new Paragraph("");
            float[] columnWidthDate = new float[]{100};
            PdfPTable tableDate = new PdfPTable(columnWidthDate);
            tableDate.setWidthPercentage(90f);
            insertCell(tableDate, "Report genrated at: " + getDateTime(), Element.ALIGN_CENTER, 3, bfBold12, true);

            paragraphLogo.add(tableLogo);
            paragraphInfo.add(tableInfo);
            paragraphData.add(table);
            paragraphDate.add(tableDate);

            doc.add(paragraphLogo);
            doc.add(paragraphName);
            doc.add(paragraphInfo);
            doc.add(paragraphData);
            doc.add(paragraphDate);
            Toast.makeText(context, "Report created", Toast.LENGTH_LONG).show();

        } catch (DocumentException dex) {
            dex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (doc != null) {
                doc.close();
            }
            if (docWriter != null) {
                docWriter.close();
            }
        }
    }

    private static void insertCell(PdfPTable table, String text, int align, int colspan, Font font, boolean isBorder) {

        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        if (!isBorder)
            cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        if (text.trim().equalsIgnoreCase("")) {
            cell.setMinimumHeight(10f);
        }
        table.addCell(cell);
    }


    public static void createCodeListPDF(Context context, List<EnterNameEntry> codeList, String pathUrl) {

        Document doc = new Document();
        PdfWriter docWriter = null;
        File dir = null;
        File file;
        try {
            //special font sizes
            Font bfBold12 = new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(FontFamily.TIMES_ROMAN, 12);
            Font bf13 = new Font(FontFamily.TIMES_ROMAN, 15, Font.NORMAL, new BaseColor(0, 0, 0));

            dir = new File(pathUrl);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            com.mobilophilia.mydairy.common.Log.e("PDFCreator", "PDF Path: " + pathUrl);
            SimpleDateFormat sdf = new SimpleDateFormat("MMyyyyhhmmss");
            file = new File(dir, "CodeList_" + sdf.format(Calendar.getInstance().getTime()) + ".pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            docWriter = PdfWriter.getInstance(doc, fOut);

            //document header attributes
            doc.addAuthor("Mahi");
            doc.addCreationDate();
            doc.addProducer();
            doc.addCreator("mobilophilia.com");
            doc.addTitle("CodeList");
            doc.setPageSize(PageSize.LETTER);
            doc.open();

            Paragraph paragraphLogo = new Paragraph("");
            //set Logo in cell
            Drawable myImage = context.getResources().getDrawable(R.drawable.ic_rsz_app_icon);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();
            Image bgImage = Image.getInstance(bitmapdata);
            bgImage.setAbsolutePosition(330f, 642f);

            PdfPCell cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.addElement(bgImage);

            PdfPTable tableLogo = new PdfPTable(1);
            tableLogo.setWidthPercentage(10f);
            tableLogo.addCell(cell);
            //End Of Logo

            //Report Name Start
            Paragraph paragraphName = new Paragraph("");
            paragraphName.setSpacingAfter(5f);
            paragraphName.setSpacingBefore(5f);
            paragraphName.add(new Phrase("CodeList", bf13));
            paragraphName.setAlignment(Element.ALIGN_CENTER);


            Paragraph paragraphData = new Paragraph("");
            float[] columnWidths = new float[]{15, 30, 30, 30, 30};
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(90f);

            //insert column headings
            insertCell(table, "#", Element.ALIGN_CENTER, 1, bfBold12, true);
            insertCell(table, "Code", Element.ALIGN_CENTER, 1, bfBold12, true);
            insertCell(table, "Name", Element.ALIGN_CENTER, 1, bfBold12, true);
            insertCell(table, "Type", Element.ALIGN_CENTER, 1, bfBold12, true);
            insertCell(table, "Phone No", Element.ALIGN_CENTER, 1, bfBold12, true);

            table.setHeaderRows(1);

            for (int i = 0; i < codeList.size(); i++) {
                insertCell(table, "" + String.valueOf((i + 1)), Element.ALIGN_CENTER, 1, bf12, true);
                EnterNameEntry nameData = codeList.get(i);

                insertCell(table, "" + nameData.getNameCode(), Element.ALIGN_CENTER, 1, bf12, true);
                insertCell(table, "" + capitalizeString(nameData.getEnterName()), Element.ALIGN_CENTER, 1, bf12, true);
                insertCell(table, "" + Util.getTypeFromSelected(nameData.getType()), Element.ALIGN_CENTER, 1, bf12, true);
                insertCell(table, "" + nameData.getPhoneNo(), Element.ALIGN_CENTER, 1, bf12, true);
            }

            Paragraph paragraphDate = new Paragraph("");
            float[] columnWidthDate = new float[]{100};
            PdfPTable tableDate = new PdfPTable(columnWidthDate);
            tableDate.setWidthPercentage(90f);
            insertCell(tableDate, "Report genrated at: " + getDateTime(), Element.ALIGN_CENTER, 3, bfBold12, true);

            paragraphLogo.add(tableLogo);
            paragraphData.add(table);
            paragraphDate.add(tableDate);

            doc.add(paragraphLogo);
            doc.add(paragraphName);
            doc.add(paragraphData);
            Toast.makeText(context, "Report created", Toast.LENGTH_LONG).show();

        } catch (DocumentException dex) {
            dex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (doc != null) {
                doc.close();
            }
            if (docWriter != null) {
                docWriter.close();
            }
        }
    }


    public static String[] formatToYesterdayOrToday(Date lastModDate) throws android.net.ParseException, java.text.ParseException {

        String[] dateTimeArray = new String[2];
        SimpleDateFormat sdf = new SimpleDateFormat("EEE hh:mma MMM d, yyyy");
        String date = sdf.format(lastModDate);
        Date dateTime = new SimpleDateFormat("EEE hh:mma MMM d, yyyy").parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);

        DateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
        DateFormat dateYear = new SimpleDateFormat("MMM d, yyyy");
        DateFormat dayTimef = new SimpleDateFormat("EEE hh:mm a");

        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            dateTimeArray[0] = "Today ";
            dateTimeArray[1] = "" + timeFormatter.format(dateTime);
            return dateTimeArray;
        } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            dateTimeArray[0] = "Yesterday ";
            dateTimeArray[1] = "" + timeFormatter.format(dateTime);
            return dateTimeArray;
        } else {
            dateTimeArray[0] = "" + dateYear.format(dateTime);
            dateTimeArray[1] = "" + dayTimef.format(dateTime);
            return dateTimeArray;
        }
    }

    public static String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}
