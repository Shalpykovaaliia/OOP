/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.components;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 *
 * @author User
 */
public class JFXInformationDialogFactory {
        public static JFXDialog create(StackPane rootPane,String headeText , String bodText, String closeBtnText)  {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXDialog fxDialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.TOP, true);
        HBox headerContent = new HBox(5);
        FontAwesomeIconView checkIcon = new FontAwesomeIconView(FontAwesomeIcon.INFO_CIRCLE);
        checkIcon.styleProperty().set("-fx-font-size: 30px;-fx-padding-right: 20px;");
        checkIcon.setFill(Color.DODGERBLUE);
        headerContent.getChildren().add(checkIcon);
        Text headerText = new Text(headeText);
        headerText.setStyle("-fx-font-size: 22px;");
        headerContent.getChildren().add(headerText);
        dialogLayout.setHeading(headerContent);
            dialogLayout.setBody(new Text(bodText));
        JFXButton closeBtn = new JFXButton(closeBtnText);
        closeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                fxDialog.close();
            }
        });
        dialogLayout.setActions(closeBtn);
        return fxDialog;
    }
}
