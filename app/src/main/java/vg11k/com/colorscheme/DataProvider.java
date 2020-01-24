package vg11k.com.colorscheme;

import android.os.Parcel;
import android.os.Parcelable;

import android.content.Context;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * Created by Julien on 30/09/2019.
 */

public class DataProvider implements Parcelable {

    public static String m_ID = "Dataprovider";
    private final String COLORCHAT_FILENAME  =  "coloref.csv";
    private final ArrayList<String[]> m_colorArray = new ArrayList<String[]>();

    public DataProvider(Context context) {
        try {
            //File csvfile = new File(Environment.getExternalStorageDirectory() + "/COLORCHAT_FILENAME");
            InputStream is = context.getAssets().open(COLORCHAT_FILENAME);
            InputStreamReader isr = new InputStreamReader(is);
            //CSVReader reader = new CSVReader(isr);
            CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
            CSVReader reader = new CSVReaderBuilder(isr)/*.withSkipLines(1)*/.withCSVParser(parser).build();
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                System.out.println(nextLine[0] + " " + nextLine[1] + " etc...");
                m_colorArray.add(nextLine);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //careful : line 0 is the header !!
    public final String[] getLine(int i) {
        return m_colorArray.get(i);
    }

    public DataProvider(Parcel parcel) {
        parcel.readList(m_colorArray, String[].class.getClassLoader());
    }

    //carefult : it include the header !!
    public final int getLinesCount() {
        return m_colorArray.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeList(m_colorArray);

       /* dest.writeInt(m_distance);
        dest.writeFloat(m_volume);
        dest.writeFloat(m_price);
        dest.writeSerializable(m_date);//writeValue(m_date);*/
    }

    public static final Creator<DataProvider> CREATOR = new Creator<DataProvider>() {
        @Override
        public DataProvider createFromParcel(Parcel in) {
            return new DataProvider(in);
        }

        @Override
        public DataProvider[] newArray(int size) {
            return new DataProvider[size];
        }
    };
}
