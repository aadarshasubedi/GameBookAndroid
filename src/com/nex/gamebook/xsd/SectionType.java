//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.09.24 at 03:56:01 PM CEST 
//


package com.nex.gamebook.xsd;

import ae.javax.xml.bind.annotation.XmlAccessType;
import ae.javax.xml.bind.annotation.XmlAccessorType;
import ae.javax.xml.bind.annotation.XmlAttribute;
import ae.javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sectionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sectionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bonuses" type="{}bonusesType" minOccurs="0"/>
 *         &lt;element name="enemies" type="{}enemiesRefType" minOccurs="0"/>
 *         &lt;element name="options" type="{}optionsType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="level" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="comment" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="resetAttributes" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="resetPositiveAttributes" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="resetNegativeAttributes" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="xpcoeff" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="position" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="scoreMultiplier" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="alreadyVisitedText" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="text" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="luckDefeatEnemies" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="enemiesDefeatedText" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="luckText" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="loseSection" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="winSection" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sectionType", propOrder = {
    "bonuses",
    "enemies",
    "options"
})
public class SectionType {

    protected BonusesType bonuses;
    protected EnemiesRefType enemies;
    protected OptionsType options;
    @XmlAttribute(name = "level")
    protected Integer level;
    @XmlAttribute(name = "comment")
    protected String comment;
    @XmlAttribute(name = "resetAttributes")
    protected Boolean resetAttributes;
    @XmlAttribute(name = "resetPositiveAttributes")
    protected Boolean resetPositiveAttributes;
    @XmlAttribute(name = "resetNegativeAttributes")
    protected Boolean resetNegativeAttributes;
    @XmlAttribute(name = "xpcoeff")
    protected Float xpcoeff;
    @XmlAttribute(name = "position", required = true)
    protected int position;
    @XmlAttribute(name = "scoreMultiplier")
    protected Float scoreMultiplier;
    @XmlAttribute(name = "alreadyVisitedText")
    protected String alreadyVisitedText;
    @XmlAttribute(name = "text", required = true)
    protected String text;
    @XmlAttribute(name = "luckDefeatEnemies")
    protected Boolean luckDefeatEnemies;
    @XmlAttribute(name = "enemiesDefeatedText")
    protected String enemiesDefeatedText;
    @XmlAttribute(name = "luckText")
    protected String luckText;
    @XmlAttribute(name = "loseSection")
    protected Boolean loseSection;
    @XmlAttribute(name = "winSection")
    protected Boolean winSection;

    /**
     * Gets the value of the bonuses property.
     * 
     * @return
     *     possible object is
     *     {@link BonusesType }
     *     
     */
    public BonusesType getBonuses() {
        return bonuses;
    }

    /**
     * Sets the value of the bonuses property.
     * 
     * @param value
     *     allowed object is
     *     {@link BonusesType }
     *     
     */
    public void setBonuses(BonusesType value) {
        this.bonuses = value;
    }

    /**
     * Gets the value of the enemies property.
     * 
     * @return
     *     possible object is
     *     {@link EnemiesRefType }
     *     
     */
    public EnemiesRefType getEnemies() {
        return enemies;
    }

    /**
     * Sets the value of the enemies property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnemiesRefType }
     *     
     */
    public void setEnemies(EnemiesRefType value) {
        this.enemies = value;
    }

    /**
     * Gets the value of the options property.
     * 
     * @return
     *     possible object is
     *     {@link OptionsType }
     *     
     */
    public OptionsType getOptions() {
        return options;
    }

    /**
     * Sets the value of the options property.
     * 
     * @param value
     *     allowed object is
     *     {@link OptionsType }
     *     
     */
    public void setOptions(OptionsType value) {
        this.options = value;
    }

    /**
     * Gets the value of the level property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * Sets the value of the level property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLevel(Integer value) {
        this.level = value;
    }

    /**
     * Gets the value of the comment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComment(String value) {
        this.comment = value;
    }

    /**
     * Gets the value of the resetAttributes property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isResetAttributes() {
        return resetAttributes;
    }

    /**
     * Sets the value of the resetAttributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setResetAttributes(Boolean value) {
        this.resetAttributes = value;
    }

    /**
     * Gets the value of the resetPositiveAttributes property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isResetPositiveAttributes() {
        return resetPositiveAttributes;
    }

    /**
     * Sets the value of the resetPositiveAttributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setResetPositiveAttributes(Boolean value) {
        this.resetPositiveAttributes = value;
    }

    /**
     * Gets the value of the resetNegativeAttributes property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isResetNegativeAttributes() {
        return resetNegativeAttributes;
    }

    /**
     * Sets the value of the resetNegativeAttributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setResetNegativeAttributes(Boolean value) {
        this.resetNegativeAttributes = value;
    }

    /**
     * Gets the value of the xpcoeff property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getXpcoeff() {
        return xpcoeff;
    }

    /**
     * Sets the value of the xpcoeff property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setXpcoeff(Float value) {
        this.xpcoeff = value;
    }

    /**
     * Gets the value of the position property.
     * 
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets the value of the position property.
     * 
     */
    public void setPosition(int value) {
        this.position = value;
    }

    /**
     * Gets the value of the scoreMultiplier property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getScoreMultiplier() {
        return scoreMultiplier;
    }

    /**
     * Sets the value of the scoreMultiplier property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setScoreMultiplier(Float value) {
        this.scoreMultiplier = value;
    }

    /**
     * Gets the value of the alreadyVisitedText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlreadyVisitedText() {
        return alreadyVisitedText;
    }

    /**
     * Sets the value of the alreadyVisitedText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlreadyVisitedText(String value) {
        this.alreadyVisitedText = value;
    }

    /**
     * Gets the value of the text property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setText(String value) {
        this.text = value;
    }

    /**
     * Gets the value of the luckDefeatEnemies property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isLuckDefeatEnemies() {
        return luckDefeatEnemies;
    }

    /**
     * Sets the value of the luckDefeatEnemies property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setLuckDefeatEnemies(Boolean value) {
        this.luckDefeatEnemies = value;
    }

    /**
     * Gets the value of the enemiesDefeatedText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnemiesDefeatedText() {
        return enemiesDefeatedText;
    }

    /**
     * Sets the value of the enemiesDefeatedText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnemiesDefeatedText(String value) {
        this.enemiesDefeatedText = value;
    }

    /**
     * Gets the value of the luckText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLuckText() {
        return luckText;
    }

    /**
     * Sets the value of the luckText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLuckText(String value) {
        this.luckText = value;
    }

    /**
     * Gets the value of the loseSection property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isLoseSection() {
        return loseSection;
    }

    /**
     * Sets the value of the loseSection property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setLoseSection(Boolean value) {
        this.loseSection = value;
    }

    /**
     * Gets the value of the winSection property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isWinSection() {
        return winSection;
    }

    /**
     * Sets the value of the winSection property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setWinSection(Boolean value) {
        this.winSection = value;
    }

}
