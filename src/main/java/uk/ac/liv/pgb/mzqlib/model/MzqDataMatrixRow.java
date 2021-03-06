package uk.ac.liv.pgb.mzqlib.model;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javafx.collections.FXCollections;

/**
 *
 * @author Da Qi
 * @since 29-Jul-2014 12:34:12
 */
public class MzqDataMatrixRow {
    private StringProperty             objectId;
    private StringProperty             objectValue;    // Sequence or accession
    private final List<StringProperty> values;

    /**
     * Constructor of MzqDataMatrixRow.
     */
    public MzqDataMatrixRow() {
        objectId    = new SimpleStringProperty("");
        objectValue = new SimpleStringProperty("");
        values      = FXCollections.observableArrayList();
    }

    /**
     * Get object id as StringProperty.
     *
     * @return the objectId
     */
    public final StringProperty objectId() {
        return objectId;
    }

    /**
     * Get value from specified position.
     *
     * @param i the position of the value
     *
     * @return the value of specific position in the list
     */
    public final StringProperty value(final int i) {
        return values.get(i);
    }

    /**
     * Set the list of values.
     *
     * @return the values
     */
    public final List<StringProperty> values() {
        return values;
    }

    /**
     * Get object id as String.
     *
     * @return the value of objectId
     */
    public final String getObjectId() {
        return objectId.get();
    }

    /**
     * Set object id.
     *
     * @param objectId the objectId to set
     */
    public final void setObjectId(final StringProperty objectId) {
        this.objectId = objectId;
    }

    /**
     * Get object value as StringProperty.
     *
     * @return the objectValue
     */
    public final StringProperty getObjectValue() {
        return objectValue;
    }

    /**
     * Set object value.
     *
     * @param objectValue the objectValue to set
     */
    public final void setObjectValue(final StringProperty objectValue) {
        this.objectValue = objectValue;
    }

    /**
     * Set list of values.
     *
     * @param values the values to set
     */
    public final void setValues(final List<String> values) {
        for (String value : values) {
            this.values.add(new SimpleStringProperty(value));
        }
    }
}
//~ Formatted by Jindent --- http://www.jindent.com
