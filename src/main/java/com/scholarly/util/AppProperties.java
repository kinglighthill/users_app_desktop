package com.scholarly.util;

import com.scholarly.models.FAQ;
import com.scholarly.models.FaqItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Properties;

public class AppProperties {
    private static AppProperties mInstance = null;

    private String applicationIdSuffix;
    private int versionCode;
    private String versionName;
    private String appSlug;
    private String appId;
    private String country;
    private boolean hasMultipleCountries;
    private boolean hasTheory;
    private boolean includeLiteraryTexts;
    private boolean hasNotes;
    private String appName;
    private String exam;
    private String school;
    private String website;
    private String dbName;
    private int dbVersion;
    private String srcPath;
    private String stagingBaseUrl;
    private String prodBaseUrl;
    private String stagingClientID;
    private String prodClientID;
    private String paystackUrl;
    private String welcomeTextOne;

    private ArrayList<FAQ> faqs = new ArrayList<>();

    private final boolean isStaging = false;

    private AppProperties() {
        try {
            InputStream propertiesStream = getClass().getClassLoader().getResourceAsStream("exam.properties");
            Properties properties = new Properties();
            properties.load(propertiesStream);

            applicationIdSuffix = properties.getProperty(PropertyKeys.APPLICATION_ID_SUFFIX);

            if (Helper.isOsType(Helper.OS_TYPE.WIN)) {
                versionCode = Integer.parseInt(properties.getProperty(PropertyKeys.VERSION_CODE_WIN));
                versionName = properties.getProperty(PropertyKeys.VERSION_NAME_WIN);
            } else if (Helper.isOsType(Helper.OS_TYPE.MAC)) {
                versionCode = Integer.parseInt(properties.getProperty(PropertyKeys.VERSION_CODE_MAC));
                versionName = properties.getProperty(PropertyKeys.VERSION_NAME_MAC);
            } else {
                versionCode = Integer.parseInt(properties.getProperty(PropertyKeys.VERSION_CODE_LINUX));
                versionName = properties.getProperty(PropertyKeys.VERSION_NAME_LINUX);
            }

            appSlug = properties.getProperty(PropertyKeys.APP_SLUG);
            appId = properties.getProperty(PropertyKeys.APP_ID);
            country = properties.getProperty(PropertyKeys.COUNTRY);
            hasMultipleCountries = Boolean.parseBoolean(properties.getProperty(PropertyKeys.HAS_MULTIPLE_COUNTRIES));
            hasTheory = Boolean.parseBoolean(properties.getProperty(PropertyKeys.HAS_THEORY));
            includeLiteraryTexts = Boolean.parseBoolean(properties.getProperty(PropertyKeys.INCLUDE_LITERARY_TEXTS));
            hasNotes = Boolean.parseBoolean(properties.getProperty(PropertyKeys.HAS_NOTES));
            appName = properties.getProperty(PropertyKeys.APP_NAME);
            exam = properties.getProperty(PropertyKeys.EXAM);
            school = properties.getProperty(PropertyKeys.SCHOOL);
            website = properties.getProperty(PropertyKeys.WEBSITE);
            dbName = properties.getProperty(PropertyKeys.DB_NAME);
            dbVersion = Integer.parseInt(properties.getProperty(PropertyKeys.DB_VERSION));
            srcPath = properties.getProperty(PropertyKeys.SRC_PATH);
            stagingBaseUrl = properties.getProperty(PropertyKeys.STAGING_BASE_URL);
            prodBaseUrl = properties.getProperty(PropertyKeys.PROD_BASE_URL);
            stagingClientID = properties.getProperty(PropertyKeys.STAGING_CLIENT_ID);
            prodClientID = properties.getProperty(PropertyKeys.PROD_CLIENT_ID);
            paystackUrl = properties.getProperty(PropertyKeys.PAYSTACK_URL);
            welcomeTextOne = properties.getProperty(PropertyKeys.WELCOME_TEXT_ONE);

            loadFAQs("exam.json");
            loadFAQs("data.json");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static synchronized AppProperties getInstance() {
        if (mInstance == null)
            mInstance = new AppProperties();

        return mInstance;
    }

    private void loadFAQs(String fileName) throws JSONException, IOException {
        InputStream jsonStream = getClass().getClassLoader().getResourceAsStream(fileName);
        assert jsonStream != null;
        InputStreamReader reader = new InputStreamReader(jsonStream);
        JSONTokener tokener = new JSONTokener(reader);
        JSONObject jsonObject = new JSONObject(tokener);
        JSONArray jsonArray = jsonObject.getJSONArray(JsonKeys.FAQS);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject faqObject = jsonArray.getJSONObject(i);

            String title = faqObject.getString(JsonKeys.FAQ_TITLE);
            JSONArray faqItemsArray = faqObject.getJSONArray(JsonKeys.FAQ_ITEMS);

            ArrayList<FaqItem> faqItems = new ArrayList<>();
            for (int j = 0; j < faqItemsArray.length(); j++) {
                JSONObject faqItemObject = faqItemsArray.getJSONObject(j);

                String question = faqItemObject.getString(JsonKeys.FAQ_QUESTION);
                String answer = faqItemObject.getString(JsonKeys.FAQ_ANSWER).replace("\n", System.lineSeparator());

                FaqItem faqItem = new FaqItem(question, answer);
                faqItems.add(faqItem);
            }

            FAQ faq = new FAQ(title, faqItems);
            faqs.add(faq);
        }

        reader.close();
    }

    public String getApplicationIdSuffix() {
        return applicationIdSuffix;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getAppSlug() {
        return appSlug;
    }

    public String getAppId() {
        return appId;
    }

    public String getCountry() {
        return country;
    }

    public boolean isHasMultipleCountries() {
        return hasMultipleCountries;
    }

    public boolean isHasTheory() {
        return hasTheory;
    }

    public boolean isIncludeLiteraryTexts() {
        return includeLiteraryTexts;
    }

    public boolean isHasNotes() {
        return hasNotes;
    }

    public String getAppName() {
        return appName;
    }

    public String getExam() {
        return exam;
    }

    public String getSchool() {
        return school;
    }

    public String getWebsite() {
        return website;
    }

    public String getDbName() {
        return dbName;
    }

    public int getDbVersion() {
        return dbVersion;
    }

    public String getSrcPath() {
        return srcPath;
    }

    public String getBaseUrl() {
        if (!isStaging) {
            return prodBaseUrl;
        } else  {
            return stagingBaseUrl;
        }
    }

    public String getClientID() {
        if (!isStaging) {
            return prodClientID;
        } else  {
            return stagingClientID;
        }
    }

    public String getPaystackUrl() {
        return paystackUrl;
    }

    public String getWelcomeTextOne() {
        return welcomeTextOne;
    }

    public ArrayList<FAQ> getFaqs() {
        return faqs;
    }

    private static class PropertyKeys {
        static String APPLICATION_ID_SUFFIX = "applicationIdSuffix";
        static String VERSION_CODE_WIN = "versionCodeWin";
        static String VERSION_NAME_WIN = "versionNameWin";
        static String VERSION_CODE_MAC = "versionCodeMac";
        static String VERSION_NAME_MAC = "versionNameMac";
        static String VERSION_CODE_LINUX = "versionCodeLinux";
        static String VERSION_NAME_LINUX = "versionNameLinux";
        static String APP_SLUG = "appSlug";
        static String APP_ID = "appId";
        static String COUNTRY = "country";
        static String HAS_MULTIPLE_COUNTRIES = "hasMultipleCountries";
        static String HAS_THEORY = "hasTheory";
        static String INCLUDE_LITERARY_TEXTS = "includeLiteraryTexts";
        static String HAS_NOTES = "hasNotes";
        static String APP_NAME = "appName";
        static String EXAM = "exam";
        static String SCHOOL = "school";
        static String WEBSITE = "website";
        static String DB_NAME = "dbName";
        static String DB_VERSION = "dbVersion";
        static String SRC_PATH = "srcPath";
        static String STAGING_BASE_URL = "stagingBaseURL";
        static String PROD_BASE_URL = "prodBaseURL";
        static String STAGING_CLIENT_ID = "stagingClientID";
        static String PROD_CLIENT_ID = "prodClientID";
        static String PAYSTACK_URL = "paystackURL";
        static String WELCOME_TEXT_ONE = "welcomeTextOne";
    }

    private static class JsonKeys {
        static String FAQS = "faqs";
        static String FAQ_TITLE = "faq_title";
        static String FAQ_ITEMS = "faq_items";
        static String FAQ_QUESTION = "question";
        static String FAQ_ANSWER = "answer";
    }
}