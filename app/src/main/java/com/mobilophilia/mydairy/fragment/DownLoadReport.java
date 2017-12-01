package com.mobilophilia.mydairy.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
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
import com.mobilophilia.mydairy.common.Constants;
import com.mobilophilia.mydairy.common.MessageEvent;
import com.mobilophilia.mydairy.common.Util;
import com.mobilophilia.mydairy.database.DBHelper;
import com.mobilophilia.mydairy.database.MyEntries;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by mukesh on 18/08/17.
 */

public class DownLoadReport extends Fragment implements View.OnClickListener {

    private RadioGroup dwlRadioGroup;
    private Button btnDownLoad;
    private ProgressBar progressBar;
    public Dialog dialog;
    private TextView dia_msg_tv;
    private String path;
    private View rootView;

    public DownLoadReport() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_download_report, container, false);

        dwlRadioGroup = (RadioGroup) rootView.findViewById(R.id.dwl_radio_group);

        btnDownLoad = (Button) rootView.findViewById(R.id.btn_download);
        btnDownLoad.setOnClickListener(this);

        Util.iSenableButton(getActivity(), btnDownLoad, false);
        path = Util.createAppFolder(getAgentId());


        dwlRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_daily_report:
                        // do operations specific to this selection
                        Util.iSenableButton(getActivity(), btnDownLoad, true);
                        break;
                    case R.id.rb_details_report:
                        //Util.iSenableButton(getActivity(), btnDownLoad, true);
                        // do operations specific to this selection
                        break;
                    case R.id.rb_price_report:
                        //Util.iSenableButton(getActivity(), btnDownLoad, true);
                        // do operations specific to this selection
                        break;
                    case R.id.rb_code_list:
                        //Util.iSenableButton(getActivity(), btnDownLoad, true);
                        // do operations specific to this selection
                        break;
                }
            }
        });

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.btn_download:
                //AppManager.getInstance().getDownloadManager().dd(getActivity());//fileDownload(getActivity(),"");
                try {
                    DBHelper dbHelper = new DBHelper(getActivity());
                    List<MyEntries> arrayMyEntries = new ArrayList<MyEntries>();
                    arrayMyEntries = dbHelper.getAllEntriesForReportByDate("", "", Util.getDate(), "", true);
                    if (arrayMyEntries.size() > 0) {
                        String[] info = new String[1];
                        info[0]= Util.getDate();
                        Util.createPDF("DailyReport",getActivity(), arrayMyEntries, path,info,true);
                        dwlRadioGroup.clearCheck();
                        Util.iSenableButton(getActivity(), btnDownLoad, false);
                    }else{
                        String errorMessage = Constants.NO_RECORD;
                        errorMessage(errorMessage);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private void errorMessage(String message) {
        Snackbar.make(rootView, "" + message, Snackbar.LENGTH_LONG).show();
    }

    private String getAgentId() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.SP_MY_DAIRY_MK, getActivity().MODE_PRIVATE);
        String id = sharedPreferences.getString(Constants.SP_AGENT_ID_KEY, null);
        return id;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getType()) {
            case MessageEvent.DOWNLOAD_REPORT_SUCCESS:
                Util.dismissBarDialog();

                break;
            case MessageEvent.DOWNLOAD_REPORT_FAILURE:
                Util.dismissBarDialog();
                Util.launchMessageDialog(getActivity(), "" + event.getMessage());
                break;
            case MessageEvent.DOWNLOAD_REPORT_PROGRESS:
                Util.dismissBarDialog();
                launchBarDialogPd(getActivity(), 0);
                break;
            case MessageEvent.DOWNLOAD_REPORT_PROGRESS_VALUE:
                com.mobilophilia.mydairy.common.Log.d("DOWNLOAD_REPORT_PROGRESS_VALUE--> ", "" + event.getIntValue());
                launchBarDialogPd(getActivity(), event.getIntValue());
                break;
            case MessageEvent.NETWORK_TIME_OUT:
                Util.dismissBarDialog();
                Toast.makeText(getActivity(), getString(R.string.network_timeout_error), Toast.LENGTH_LONG).show();
                break;

            case MessageEvent.SERVER_ERROR_OCCURRED:
                Util.dismissBarDialog();
                Toast.makeText(getActivity(), getString(R.string.server_error), Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void launchBarDialogPd(Activity activity, int value) {
        if (dialog != null) {
            progressBar.setProgress(value);
            dia_msg_tv.setText("" + value);
        } else {
            dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.progress_bar);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = dialog.getWindow();
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);

            progressBar = (ProgressBar) dialog.findViewById(R.id.progress_tv);
            progressBar.setMax(100);
            dia_msg_tv = (TextView) dialog.findViewById(R.id.dia_msg);
            dialog.show();
        }
    }

    public void dismissBarDialogPd() {
        try {
            if (dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /*public void createPDF(List<MyEntries> arrayMyEntries,String pathUrl) throws FileNotFoundException, DocumentException {

        //create document file
        Document doc = new Document();
        try {

            com.mobilophilia.mydairy.common.Log.e("PDFCreator", "PDF Path: " + pathUrl);
            SimpleDateFormat sdf = new SimpleDateFormat("MMyyyyhhmmss");
            file = new File(dir, "DailyReport" + "_" + sdf.format(Calendar.getInstance().getTime()) + ".pdf");
            FileOutputStream fOut = new FileOutputStream(file);
            PdfWriter writer = PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();
//create table
            PdfPTable pt = new PdfPTable(3);
            pt.setWidthPercentage(100);
            float[] fl = new float[]{20, 40, 40};
            pt.setWidths(fl);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //set drawable in cell
            Drawable myImage = getActivity().getResources().getDrawable(R.mipmap.ic_launcher);
            Bitmap bitmap = ((BitmapDrawable) myImage).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();
            try {
                bgImage = Image.getInstance(bitmapdata);
                bgImage.setAbsolutePosition(330f, 642f);
                cell.addElement(bgImage);
                pt.addCell(cell);
                cell = new PdfPCell();
                cell.setPaddingTop(40);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.addElement(new Paragraph("Daily Report"));
                pt.addCell(cell);


                // AVG THINGS
                PdfPTable pTabled = new PdfPTable(1);
                pTabled.setWidthPercentage(100);
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(1);
                int count = arrayMyEntries.size();

                MyEntries me = arrayMyEntries.get((count - 1));

                Double fat = me.avgFat / count;
                Double snf = me.avgSnf / count;
                Double price = me.avgPrice / count;
                Double ltr = me.totalLtr;
                Double gtotal = me.grandPrice;


                cell.addElement(new Paragraph("Avg FAT: " + String.format("%.1f", fat)));
                cell.addElement(new Paragraph("Avg SNF: " + String.format("%.1f", snf)));
                cell.addElement(new Paragraph("Avg Price: " + String.format("%.2f", price)));
                cell.addElement(new Paragraph("Total Litre: " + String.format("%.1f", ltr)));
                cell.addElement(new Paragraph("Grand Total: " + String.format("%.2f", gtotal)));
                pt.addCell(cell);
                pTabled.addCell(cell);

//end


                PdfPTable pTable = new PdfPTable(1);
                pTable.setWidthPercentage(100);
                cell = new PdfPCell();
                cell.setColspan(1);
                cell.addElement(pt);
                pTable.addCell(cell);
                PdfPTable table = new PdfPTable(8);

                float[] columnWidth = new float[]{10, 30, 30, 30, 30, 30, 30, 30};
                table.setWidths(columnWidth);


                cell = new PdfPCell();


                cell.setBackgroundColor(myColor);
                cell.setColspan(8);
                cell.addElement(pTable);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(" "));
                cell.setColspan(8);
                table.addCell(cell);
                cell = new PdfPCell();
                cell.setColspan(8);

                cell.setBackgroundColor(myColor1);

                cell = new PdfPCell(new Phrase("#"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Code"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Name"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("FAT"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("SNF"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Price"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Litre"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Total"));
                cell.setBackgroundColor(myColor1);
                table.addCell(cell);

                //table.setHeaderRows(3);
                cell = new PdfPCell();
                cell.setColspan(8);

                for (int i = 0; i < arrayMyEntries.size(); i++) {
                    table.addCell(String.valueOf((i + 1)));
                    MyEntries entries = arrayMyEntries.get(i);
                    table.addCell("" + entries.getCode());
                    table.addCell("" + entries.getName());
                    table.addCell("" + entries.getFat());
                    table.addCell("" + entries.getSnf());
                    table.addCell("" + entries.getPrice());
                    table.addCell("" + entries.getLtr());
                    table.addCell("" + entries.getTotal());
                }

                PdfPTable ftable = new PdfPTable(8);
                ftable.setWidthPercentage(100);
                float[] columnWidthaa = new float[]{30, 30, 30, 30, 30, 30, 30, 30};
                ftable.setWidths(columnWidthaa);
                cell = new PdfPCell();
                cell.setColspan(8);
                cell.setBackgroundColor(myColor1);
                cell = new PdfPCell(new Phrase("Total Nunber"));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(myColor1);
                ftable.addCell(cell);
                cell = new PdfPCell(new Paragraph("Footer"));
                cell.setColspan(8);
                ftable.addCell(cell);
                cell = new PdfPCell();
                cell.setColspan(8);
                cell.addElement(ftable);
                table.addCell(cell);
                doc.add(table);
                Toast.makeText(getActivity(), "Report created", Toast.LENGTH_LONG).show();
            } catch (DocumentException de) {
                com.mobilophilia.mydairy.common.Log.e("PDFCreator", "DocumentException:" + de);
            } catch (IOException e) {
                com.mobilophilia.mydairy.common.Log.e("PDFCreator", "ioException:" + e);
            } finally {
                doc.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


}
