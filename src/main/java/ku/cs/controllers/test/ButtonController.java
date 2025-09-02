package ku.cs.controllers.test;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import ku.cs.components.*;

public class ButtonController {
    @FXML private VBox parentVBox;
    @FXML private Button buttonStandalone;
    @FXML private Button buttonInHBox;
    @FXML private Button buttonInVBox;

    @FXML
    private void initialize() {
        // === 0) เตรียมพื้นที่แสดงผล ===
        parentVBox.setSpacing(10);

        // === 1) สร้างไอคอนเดี่ยวเพื่อทดสอบ Icon node ===
        Icon loneIcon = new Icon(Icons.ARROW_LEFT, 24);

        // === 2) ทดสอบ FilledButton (ปุ่มตัวหนังสือ) ===
        FilledButton fbDefault = new FilledButton("FilledButton");
        System.out.println("[fbDefault] label=" + fbDefault.getLabel());

        // small() แบบไม่มี/มี label
        FilledButton fbSmallEmpty = FilledButton.small();
        FilledButton fbSmall = FilledButton.small("Small FB");
        System.out.println("[fbSmallEmpty] styleClass=" + fbSmallEmpty.getStyleClass());
        System.out.println("[fbSmall] styleClass=" + fbSmall.getStyleClass());

        // factory: icon(...) จะได้ FilledButtonWithIcon (subclass)
        FilledButton fbIconFactoryA = FilledButton.icon("FB Icon A");
        FilledButton fbIconFactoryB = FilledButton.icon("FB Icon B", Icons.USER);
        FilledButton fbIconFactoryC = FilledButton.icon("FB Icon C", Icons.GEAR, Icons.ARROW_RIGHT);

        // mask แบบ MEDIUM กับปุ่มธรรมดา
        Button maskedBtnMedium = new Button("Masked MEDIUM");
        FilledButton.mask(maskedBtnMedium); // = MEDIUM.mask

        // mask แบบ SMALL กับปุ่มธรรมดา
        Button maskedBtnSmall = new Button("Masked SMALL");
        FilledButton.SMALL.mask(maskedBtnSmall);

        // === 3) ทดสอบ FilledButtonWithIcon แบบกำหนดเองครบชุด ===
        // === Default ===
        FilledButtonWithIcon fwiDefault = new FilledButtonWithIcon("Default FWI", Icons.USER);

        // === Small ===
        FilledButtonWithIcon fwiSmall = FilledButtonWithIcon.small("Small FWI", Icons.GEAR, Icons.ARROW_RIGHT);

        FilledButtonWithIcon fbiMedium = FilledButtonWithIcon.medium("Small FWI", Icons.GEAR, Icons.ARROW_RIGHT);

        // === Medium ===
        // ใช้ StyleMasker.MEDIUM.mask(...) กับ Button ธรรมดา
        Button mediumBtn = new Button("Medium masked");
        FilledButton.MEDIUM.mask(mediumBtn);

        // หรือสร้าง FilledButton แล้วเพิ่มคลาส "medium" เอง
        FilledButton fbMedium = new FilledButton("Medium FB");
        fbMedium.getStyleClass().add("medium");

        FilledButtonWithIcon fwi = new FilledButtonWithIcon("WithIcon", Icons.EYE, Icons.ARROW_RIGHT);
        // ข้อความ (ใช้ชื่อ setLabelText เพื่อเลี่ยง final setText() ของ Labeled)
        fwi.setLabelText("Confirm");
        System.out.println("[fwi] text=" + fwi.getLabelText());

        // เปลี่ยน icon ซ้าย/ขวา
        fwi.setIconLeft(Icons.SMILEY);
        fwi.setIconRight(Icons.USER);

        // ปรับขนาดปุ่ม (กว้างxสูง / เฉพาะด้าน)
        fwi.setButtonSize(Region.USE_COMPUTED_SIZE, 40);
        fwi.setButtonHeight(40);
        System.out.println("[fwi] prefW=" + fwi.getButtonPrefWidth() + ", prefH=" + fwi.getButtonPrefHeight());

        // ปรับ spacer ซ้าย/ขวา
        fwi.setSpaceLeft(8);
        fwi.setSpaceRight(12);
        System.out.println("[fwi] spaceL=" + fwi.getSpaceLeft() + ", spaceR=" + fwi.getSpaceRight());

        // ปรับขนาดไอคอน (รวม/แยก)
        fwi.setIconSize(22);
        fwi.setLeftIconSize(20);
        fwi.setRightIconSize(24);

        // getter พวก node ภายใน (เอาไว้ตกแต่งเพิ่ม)
        Icon leftNode = fwi.getLeftIconNode();
        Icon rightNode = fwi.getRightIconNode();
        System.out.println("[fwi] leftNode=" + leftNode + ", rightNode=" + rightNode);

        // ทดลอง disable state
        fwi.setDisable(true);

        // === 4) ทดสอบ setTextLabel(..) ใส่ Label ใหม่ทั้งก้อน ===
        // (ถ้าอยากใช้ Label subclass เองก็ใส่มาได้)
        fwi.setTextLabel(new javafx.scene.control.Label("รีแบรนด์ใหม่"));
        fwi.setLabelText("รีแบรนด์ใหม่ (ผ่าน setLabelText)");

        // === 5) ทดสอบ StyleMasker ของ FilledButtonWithIcon กับ Button ปกติ ===
        Button plainBtnToIconMask = new Button("Mask to WithIcon");
        // ใส่ไอคอนซ้าย/ขวาตอน mask
        FilledButtonWithIcon.mask(plainBtnToIconMask, Icons.ARROW_LEFT, Icons.ARROW_RIGHT);

        // === 6) ทดสอบ replace / from / fromAndReplace ===
        // 6.1) replaceInParentOf(...) : นำ fwi ไปแทนที่ buttonInHBox
        fwi.replaceInParentOf(buttonInHBox);

        // 6.2) from(...) : clone properties จาก buttonInVBox -> ได้เป็น FilledButton
        FilledButton fbFromVBox = FilledButton.from(buttonInVBox);
        // แล้วค่อย replace เข้าไปแทนของเก่า
        fbFromVBox.replaceInParentOf(buttonInVBox);

        // 6.3) fromAndReplace(...) : ย่อหน้าเดียวแทนสองบรรทัด
        // (โชว์ให้เห็นว่าทำได้ แต่อย่าแทนของที่แทนไปแล้วอีกครั้ง)
        FilledButton fbFromStandalone = FilledButton.fromAndReplace(buttonStandalone);
        System.out.println("[fromAndReplace] done -> " + fbFromStandalone.getLabel());

        // === 7) เพิ่มของให้เห็นภาพรวมใน HBox ทดลอง ===
        //   - loneIcon
        //   - fbDefault / fbSmallEmpty / fbSmall
        //   - fbIconFactoryA/B/C (จาก factory icon)
        //   - maskedBtnMedium / maskedBtnSmall (masker)
        //   - plainBtnToIconMask (with-icon mask)
        // หมายเหตุ: fwi ถูก replace ไปแทน buttonInHBox แล้ว จึงไม่ต้อง add ลง parentVBox ซ้ำ
        fbIconFactoryB.setPrefWidth(300);
        parentVBox.getChildren().addAll(
                loneIcon,
                fbDefault, fbSmallEmpty, fbSmall, fwiDefault, fwiSmall, fbiMedium,
                fbIconFactoryA, fbIconFactoryB, fbIconFactoryC,
                maskedBtnMedium, maskedBtnSmall,
                plainBtnToIconMask
        );

        // === 8) ตรวจ getters เพิ่มเติมให้ครบ (log) ===
        System.out.println("[fbDefault] styleClass=" + fbDefault.getStyleClass());
        System.out.println("[fbIconFactoryA] class=" + fbIconFactoryA.getClass().getSimpleName());
        System.out.println("[plainBtnToIconMask] styleClass=" + plainBtnToIconMask.getStyleClass());
    }

}
