package vg11k.com.colorscheme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import org.apache.commons.lang3.ObjectUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import vg11k.com.colorscheme.schemeGenerator.AbstractSchemeGeneratorLineModel;
import vg11k.com.colorscheme.schemeGenerator.ImageMiniPreviewLineModel;
import vg11k.com.colorscheme.utils.InterfaceAdapter;


/**
 * Created by Julien on 30/09/2019.
 */

public class DataProvider implements Parcelable {

    public static String m_ID = "Dataprovider";
    private final String COLORCHAT_FILENAME  =  "coloref.csv";
    private final ArrayList<String[]> m_colorArray = new ArrayList<String[]>();

    private final String DIRNAME = "SCHEMES_";

    //public static final String m_localFolderName = "local";
    //public static final String m_webFolderName = "web";

    //private String m_localFolderPath = "";
    //private String m_webFolderPath = "";

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

        StorageKind.initializePath(context);
        //StorageKind storage = StorageKind.LOCAL;


        //m_localFolderPath = getAndCreateIfMissingFolderName(context, m_localFolderName);
        //m_webFolderPath = getAndCreateIfMissingFolderName(context, m_webFolderName);
    }

    //careful : line 0 is the header !!
    public final String[] getLine(int i) {
        return m_colorArray.get(i);
    }

    public final String getColorRGB(int i) { return m_colorArray.get(i)[0];}

    public final String getColorProvider(int i) { return m_colorArray.get(0)[i+1];}

    public final String getColorNameForProvider(int colorIndex, int providerIndex) {
        //it takes header & column 0 into account
        return m_colorArray.get(colorIndex+1)[providerIndex+1];
    }

    public DataProvider(Parcel parcel) {
        parcel.readList(m_colorArray, String[].class.getClassLoader());
    }

    //carefult : it include the header !!
    public final int getLinesCount() {
        return m_colorArray.size();
    }

    public final int getProviderCount() {return m_colorArray.get(0).length - 1;}

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



    public SchemeModel getSchemeGeneratorsData(Context context, String fileNameToRead) {

        //final Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
        String jsonData = read(context, fileNameToRead);
        SchemeModel model;
        if(jsonData != null) {
            Type listType = new TypeToken<SchemeModel>(){}.getType();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(AbstractSchemeGeneratorLineModel.class, new InterfaceAdapter());
            Gson gson = builder.serializeNulls().disableHtmlEscaping().create();
            model = gson.fromJson(jsonData, listType);

            //try to load the image if it exist
            Bitmap bitmap = getBitmapByName(context, model.getName(), StorageKind.LOCAL.getFullPath());//m_localFolderName);
            if(bitmap != null) {
                ((ImageMiniPreviewLineModel) model.getLines().get(0)).setBitMap(bitmap);
            }
            /*String filename = "/device/" + model.getName() + ".PNG";
            filename = filename.replace("/device", m_localFolderPath);

            File tmpFile = new File(filename);
            if(tmpFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(filename);
                ((ImageMiniPreviewLineModel) model.getLines().get(0)).setBitMap(bitmap);
            }*/


        }
        else {
            //model = new SchemeModel();
            throw new IllegalStateException("Error : a gson request should be on an existing file !");
        }

        /*if(jsonData != null) {
            Type listType = new TypeToken<ArrayList<AbstractSchemeGeneratorLineModel>>(){}.getType();
            models = new Gson().fromJson(jsonData, listType);
        }
        else {
            models = new ArrayList<AbstractSchemeGeneratorLineModel>();
        }*/

        return model;
    }

    public Bitmap getBitmapByName(Context context, String name, String folderPath) {

        /*if(m_webFolderPath.isEmpty())
            m_webFolderPath = getAndCreateIfMissingFolderName(context, m_webFolderName);
        if(m_localFolderPath.isEmpty())
            m_localFolderPath = getAndCreateIfMissingFolderName(context, m_localFolderName);*/

        Bitmap result = null;

        /*String pathToUse = m_localFolderPath;
        if(folder.equals(m_webFolderName)) {
            pathToUse = m_webFolderPath;
        }*/

        String filename = folderPath + name + ".PNG";

        //String filename = "/device/" + name + ".PNG";


        //filename = filename.replace("/device", pathToUse);

        File tmpFile = new File(filename);
        if(tmpFile.exists()) {
            result = BitmapFactory.decodeFile(filename);
        }

        return result;

    }


    public boolean persistSchemeGeneratorData(Context context, SchemeModel model) {

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(AbstractSchemeGeneratorLineModel.class, new InterfaceAdapter());
        Gson gson = builder.serializeNulls().disableHtmlEscaping().create();

        String formattedData = gson.toJson(model);
        boolean done = create(context, model.getName(), formattedData);

        if(done) {

            String filename = StorageKind.LOCAL.getFullPath() + model.getName() + ".PNG";
            //String filename = "/device/" + model.getName() + ".PNG";
            //filename = filename.replace("/device", m_localFolderPath);

            Bitmap bitmap = ((ImageMiniPreviewLineModel) model.getLines().get(0)).getBitMap();
            done = done & persistBitmap(filename, bitmap);
        }


        return done;
    }

    public boolean persistBitmap(String pathName, Bitmap bmp) {

        boolean done = true;

        try {
            FileOutputStream out = new FileOutputStream(pathName);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (IOException e) {
            e.printStackTrace();
            done = false;
        }

        return done;

    }

    public boolean persistSchemeGeneratorsData(Context context, ArrayList<AbstractSchemeGeneratorLineModel> models) {

        final Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
        String formattedData = gson.toJson(models);
        return create(context, formattedData);
    }

    public boolean isFilePresent(Context context) {
        String path = context.getFilesDir().getAbsolutePath() + DIRNAME;
        File file = new File(path);
        return file.exists();
    }

    public ArrayList<String> getLocalJson(Context context) {

        ArrayList<String> resultNames = new ArrayList<String>();

        try {
            //maybe remove the last slash ?
            String folderName = StorageKind.LOCAL.getFullPath();//getAndCreateIfMissingFolderName(context, m_localFolderName);
            File folder = new File(folderName);

            File[] fileList = folder.listFiles();
            for (File file : fileList) {
                if (file.getName().contains(".json")) {
                    resultNames.add(file.getName());
                }
            }
        }
        catch(NullPointerException e) {
            System.out.println("Error in localJson : " + e.getMessage());
        }

        return resultNames;
    }

    private String read(Context context, String fileNameToRead){

       /* if(m_localFolderPath.isEmpty()) {
            m_localFolderPath = getAndCreateIfMissingFolderName(context, m_localFolderName);
        }*/

        FileInputStream fis = null;
        try {

            //fis = new FileInputStream (new File(m_localFolderPath + "/" + fileNameToRead + ".json"));
            fis = new FileInputStream (new File(StorageKind.LOCAL.getFullPath() + fileNameToRead + ".json"));


                    //context.openFileInput(m_localFolderPath + "/" + fileNameToRead + ".json"); //TODO add check all files
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (FileNotFoundException fileNotFound) {
            System.out.println(fileNotFound.getStackTrace());
        } catch (IOException ioException) {
            System.out.println(ioException.getStackTrace());
        }

        try {
            fis.close();
        }
        catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        return null;
    }

    private String getAndCreateIfMissingFolderName(Context context, String folderName) {

        String intStorageDirectory = context.getFilesDir().toString();
        File folder = new File(intStorageDirectory);
        if(!folder.exists())
            folder.mkdirs();

        File subFolder = new File(folder.getAbsolutePath() + "/" + folderName);
        if(!subFolder.exists())
            subFolder.mkdirs();

        return subFolder.getAbsolutePath();
    }

    private boolean create(Context context, String name, String jsonString) {

        /*if(m_localFolderPath.isEmpty()) {
            m_localFolderPath = getAndCreateIfMissingFolderName(context, m_localFolderName);
        }*/

       /* File directory = new File("/data/com.vg11k.ColorScheme/");
        if (!directory.exists()) {
            directory.mkdir();
        }*/

        //File device = Environment.getDataDirectory();


        //String filename = "/device/" + name + ".json";

        //private directory
        //filename = filename.replace("/device", m_localFolderPath);
        String filename = StorageKind.LOCAL.getFullPath() + name + ".json";


        File tempFile = new File(filename);

        try {
            FileOutputStream fos =  new FileOutputStream(tempFile);//context.openFileOutput(DIRNAME + name+ ".json", Context.MODE_PRIVATE); //TODO add check all files
            if (jsonString != null) {
                fos.write(jsonString.getBytes());
            }
            fos.close();

        } catch (FileNotFoundException fileNotFound) {
            System.out.println("FileOutputStream exception: - " + fileNotFound.toString());
            return false;
        } catch (IOException ioException) {
            System.out.println("FileOutputStream exception: - " + ioException.toString());
            return false;
        }

        return true;
    }

    //never used if i'm right...
    private boolean create(Context context, String jsonString){
        try {
            FileOutputStream fos = context.openFileOutput(DIRNAME,Context.MODE_PRIVATE); //TODO add check all files
            if (jsonString != null) {
                fos.write(jsonString.getBytes());
            }
            fos.close();
            return true;
        } catch (FileNotFoundException fileNotFound) {
            return false;
        } catch (IOException ioException) {
            return false;
        }
    }

    public void deleteFile(String s, Context context) {
        /*if(m_localFolderPath.isEmpty()) {
            m_localFolderPath = getAndCreateIfMissingFolderName(context, m_localFolderName);
        }
        String fileNameAndPath = m_localFolderPath + "/" + s + ".json";*/
        String fileNameAndPath = StorageKind.LOCAL.getFullPath() + s;
        File fdelete = new File(fileNameAndPath);
        if(fdelete.exists()) {
            if (fdelete.delete()) {
                System.out.println("file Deleted :" + fileNameAndPath);
            } else {
                System.out.println("file not Deleted :" + fileNameAndPath);
            }
        }
        else {
            System.out.println("The file " + fileNameAndPath + " does not exist and so can't be deleted");

        }



    }
}
