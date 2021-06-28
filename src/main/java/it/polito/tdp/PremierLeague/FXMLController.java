/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnConnessioneMassima"
    private Button btnConnessioneMassima; // Value injected by FXMLLoader

    @FXML // fx:id="btnCollegamento"
    private Button btnCollegamento; // Value injected by FXMLLoader

    @FXML // fx:id="txtMinuti"
    private TextField txtMinuti; // Value injected by FXMLLoader

    @FXML // fx:id="cmbMese"
    private ComboBox<Integer> cmbMese; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM1"
    private ComboBox<Match> cmbM1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM2"
    private ComboBox<Match> cmbM2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doConnessioneMassima(ActionEvent event) {

    	txtResult.clear();
    	
    	String minS=this.txtMinuti.getText();
    	Integer min=0;
    	try {
    		min=Integer.parseInt(minS);
    	}catch(NumberFormatException e)
    	{
    		e.printStackTrace();
    	}
    	Integer mese=this.cmbMese.getValue();
    	if(mese==null)
    	{
    		txtResult.appendText("MESE NON INSERITO");
    	}else
    	{
    		List<Adiacenza> OK=new ArrayList<Adiacenza>(this.model.migliori(mese, min));
    		for(Adiacenza a:OK)
    		{
    			txtResult.appendText(a.toString()+"\n");
    		}
    	}
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    
    	txtResult.clear();
    	String minS=this.txtMinuti.getText();
    	Integer min=0;
    	try {
    		min=Integer.parseInt(minS);
    	}catch(NumberFormatException e)
    	{
    		e.printStackTrace();
    	}
    	Integer mese=this.cmbMese.getValue();
    	if(mese==null)
    	{
    		txtResult.appendText("MESE NON INSERITO");
    	}else
    	{
    		this.model.creaGrafo(mese, min);
    		txtResult.appendText("GRAFO CREATO!\n");
    		txtResult.appendText("# vertici : "+this.model.getVertici()+"\n");
    		txtResult.appendText("# archi : "+this.model.getArchi()+"\n");
    	}
    	
    	this.cmbM1.getItems().addAll(this.model.getPartite());
    	this.cmbM2.getItems().addAll(this.model.getPartite());
    }

    @FXML
    void doCollegamento(ActionEvent event) {
    	txtResult.clear();
    	Match m1=this.cmbM1.getValue();
    	Match m2=this.cmbM2.getValue();
    	if(m1==null || m2==null)
    	{
    		txtResult.appendText("compilare tutti i campi!\n");
    	}else if(m1.equals(m2))
    	{
    		txtResult.appendText("stesso match!\n");
    	}else
    	{
    		List<Match> best=new ArrayList<Match>(this.model.getListaBest(m1, m2));
    		txtResult.appendText("IL PERCORSO CON PESO MASSIMO HA PESO : "+this.model.pesoMax+" ED E' COMPOSTO DA:\n");
    		for(Match m:best)
    		{
    			txtResult.appendText(m.toString()+"\n");
    		}
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnConnessioneMassima != null : "fx:id=\"btnConnessioneMassima\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCollegamento != null : "fx:id=\"btnCollegamento\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMinuti != null : "fx:id=\"txtMinuti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbMese != null : "fx:id=\"cmbMese\" was not injected: check your FXML file 'Scene.fxml'.";        assert cmbM1 != null : "fx:id=\"cmbM1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbM2 != null : "fx:id=\"cmbM2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    this.model=model;
    List<Integer> mesi=new ArrayList<Integer>();
    for(int i=1;i<=12;i++)
    {
    	mesi.add(i);
    }
    this.cmbMese.getItems().addAll(mesi);
    }
    
    
}
