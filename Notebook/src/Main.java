import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import static javafx.application.Application.launch;

/**
 * 界面逻辑控制器，
 */
@SuppressWarnings("unused")
public class Main implements Initializable {
    private Stage stage;
    private File fileOpened;

    @FXML
    private TextArea textArea;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    void setStage(Stage stage) {
        this.stage = stage;
        openFile(null);
    }

    private void openFile(File file) {
        fileOpened = file;
        if (fileOpened == null) {
            stage.setTitle("CodePad");
        } else {
            stage.setTitle(fileOpened.getAbsolutePath());
        }
    }

    private void readFile(File file) {
        if (file == null) {
            textArea.setText("");
            return;
        }
        try {
            textArea.setText(new String(Files.readAllBytes(Paths.get(file.getAbsolutePath()))));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("file open error: " + e.getMessage());
            alert.show();
        }
    }

    private void saveFileAs(File file) {
        try {
            Files.write(Paths.get(file.getAbsolutePath()), textArea.getText().getBytes());
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("file write error: " + e.getMessage());
            alert.show();
        }
        openFile(file);
    }

    /**
     * 功能未实现时调用
     * @param event
     */
    @FXML
    private void onNotImplementedItemClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        String text = ((MenuItem) event.getTarget()).getText();
        alert.setContentText(text + " not implemented");
        alert.show();
    }

    /**
     * 新建文件
     * @param event
     */
    @FXML
    private void onFileNewClick(ActionEvent event) {
        openFile(null);
        readFile(null);
    }

    /**
     * 打开文件
     * @param event
     */
    @FXML
    private void onFileOpenClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        File file = fileChooser.showOpenDialog(stage);
        if (file == null) {
            // 选择文件被取消才会是null,
            return;
        }
        openFile(file);
        readFile(file);
    }

    /**
     * 文件保存
     * @param event
     */
    @FXML
    private void onFileSaveClick(ActionEvent event) {
        if (fileOpened == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("no file opened");
            alert.show();
            return;
        }
        saveFileAs(fileOpened);
    }

    /**
     * 文件另存为
     * @param event
     */
    @FXML
    private void onFileSaveAsClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File As");
        saveFileAs(fileChooser.showSaveDialog(stage));
    }

    /**
     * 插入日期功能
     * @param event
     */
    @FXML
    private void onDateClick(ActionEvent event) {
        // 格式化当前的日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        // 追加到内容里
        textArea.appendText(date);
    }


    /**
     * 实现复制功能
     */
    @FXML
    private void onCopyClick() {
        // 获取系统的剪切板
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        // 获取选中的内容
        String copyText = textArea.getSelectedText();

        System.out.println("copyText = " + copyText);
        // 封装文本内容
        Transferable trans = new StringSelection(copyText);
        // 把文本内容设置到系统剪贴板
        clipboard.setContents(trans, null);
    }

    /**
     * 实现粘贴功能
     * @param event
     */
    @FXML
    private void onPasteClick(ActionEvent event) {
        // 获取系统剪贴板
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        // 获取剪贴板中的内容
        Transferable trans = clipboard.getContents(null);

        if (trans != null) {
            // 判断剪贴板中的内容是否支持文本
            if (trans.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    // 获取剪贴板中的文本内容
                    String text = (String) trans.getTransferData(DataFlavor.stringFlavor);
                    // 粘贴
                    textArea.appendText(text);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 实现查找功能
     */
    @FXML
    private void onFindClick() {
        Stage stage = new Stage();
        stage.setTitle("查找替换");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // 添加查找框
        Label findText = new Label("查找:");
        grid.add(findText, 0, 1);

        TextField findTextField = new TextField();
        grid.add(findTextField, 1, 1);

        // 添加替换框
        Label replaceText = new Label("替换:");
        grid.add(replaceText, 0, 2);

        TextField replaceTextField = new TextField();
        grid.add(replaceTextField, 1, 2);

        // 添加按钮
        Button btn1 = new Button("查找");
        HBox hbBtn1 = new HBox(10);
        hbBtn1.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn1.getChildren().add(btn1);
        grid.add(hbBtn1, 0, 4);

        // 给查找按钮添加方法
        btn1.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                // 获取文本框中所有内容
                String text = textArea.getText();
                // 获取查找框的文本内容
                String findWords =  findTextField.getText();
                // 不为空，则进行查找
                if(findWords != null && !findWords.isEmpty()) {

                    // 查找出内容，高亮显示
                    Text t = new Text(findWords);
                    // 设置字体颜色为红色
                    t.setFill(Color.RED);
                    text = text.replaceAll(findWords, t.toString());
                    // 清空原来的内容
                    textArea.clear();
                    textArea.appendText(text);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("请输入内容!");
                    alert.show();
                }
            }
        });

        Button btn2 = new Button("全部替换");
        HBox hbBtn2 = new HBox(10);
        hbBtn2.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn2.getChildren().add(btn2);
        grid.add(hbBtn2, 1, 4);

        // 给替换按钮添加方法
        btn2.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                // 获取文本框中所有内容
                String text = textArea.getText();
                // 获取查找框的文本内容
                String findWords =  findTextField.getText();
                // 获取替换框的文本内容
                String replaceWords = replaceTextField.getText();
                // 不为空，则进行替换
                if(findWords != null && !findWords.isEmpty() && replaceWords != null && !replaceWords.isEmpty()) {
                    System.out.println("text1 = " + text);
                    // 进行替换
                    text = text.replaceAll(findWords, replaceWords);
                    System.out.println("text2 = " + text);
                    // 清空原来的内容
                    textArea.clear();
                    textArea.appendText(text);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("请输入内容!");
                    alert.show();
                }
            }
        });

        Scene scene = new Scene(grid, 300, 275);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    private void onFileQuitClick(ActionEvent event) {
        Platform.exit();
    }

    public static void main(String[] args){
        launch(args);
    }
}

