package com.wordpress.randomexplorations.personalanalytics;

import android.database.Cursor;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by maniksin on 8/21/16.
 */
public class Dom {

    public static final String DATA_ENTRY_TABLE_ROOT = "DataEntryRoot";
    public static final String DATA_ENTRY_TABLE_ELEMENT = "DataEntry";

    public Dom() { }

    private static Document create_Document_from_cursor(Cursor c) {

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement(DATA_ENTRY_TABLE_ROOT);
            doc.appendChild(rootElement);

            List<String> fields = Schema.DataEntry.get_fields();

            //c.moveToFirst();  => User can ask to generate from somewhere middle till end
            while (!c.isAfterLast()) {

                Element entry = doc.createElement(DATA_ENTRY_TABLE_ELEMENT);

                for (int i = 0; i < fields.size(); i++) {

                    Element field = doc.createElement(fields.get(i));
                    Text field_value;

                    int col_index;
                    try {
                        col_index = c.getColumnIndexOrThrow(fields.get(i));
                        if (Schema.DataEntry.is_column_long(fields.get(i))) {
                            Long value = c.getLong(col_index);
                            field_value = doc.createTextNode(String.valueOf(value));
                        } else {
                            String value = c.getString(col_index);
                            field_value = doc.createTextNode(value);
                        }
                    } catch (Exception e) {
                        // Field not present in the entry
                        field_value = doc.createTextNode("");
                    }

                    field.appendChild(field_value);
                    entry.appendChild(field);
                }

                rootElement.appendChild(entry);
                c.moveToNext();
            }

            return doc;

        } catch (Exception e) {
            Log.d("this", "Exception: " + e.getMessage());
            return null;
        }

    }

    public static String create_XML_string_from_cursor(Cursor c) {
        Document doc = create_Document_from_cursor(c);
        try {
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.toString();
        } catch (Exception ex) {
            Log.d("this", "Exception during create_XML: " + ex.getMessage());
            return null;
        }
    }
}
