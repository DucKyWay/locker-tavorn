package ku.cs.controllers.test;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import ku.cs.components.*;
import ku.cs.components.button.*;

public class ButtonController {
    @FXML private VBox parentVBoxFilled;
    @FXML private Button buttonInHBoxFilled;
    @FXML private Button buttonInVBoxFilled;

    @FXML private VBox parentVBoxElevated;
    @FXML private Button buttonInHBoxElevated;
    @FXML private Button buttonInVBoxElevated;

    @FXML private VBox parentVBoxOutlined;
    @FXML private Button buttonInHBoxOutlined;
    @FXML private Button buttonInVBoxOutlined;

    @FXML
    private void initialize() {
        // === 0) เตรียมพื้นที่แสดงผล ===
        parentVBoxFilled.setSpacing(4);

        // === 2) ทดสอบ FilledButton (ปุ่มตัวหนังสือ) ===
        FilledButton fbDefault = new FilledButton("FilledButton");
        System.out.println("[fbDefault] label=" + fbDefault.getLabel());

        // small() แบบไม่มี/มี label
        FilledButton fbSmallEmpty = FilledButton.small();
        FilledButton fbSmall = FilledButton.small("Small FB");
        System.out.println("[fbSmallEmpty] styleClass=" + fbSmallEmpty.getStyleClass());
        System.out.println("[fbSmall] styleClass=" + fbSmall.getStyleClass());

        // factory: icon(...) จะได้ FilledButtonWithIcon (subclass)
        FilledButtonWithIcon fbIconFactoryA = FilledButton.icon("FB Icon A");
        FilledButtonWithIcon fbIconFactoryB = FilledButton.icon("FB Icon B", Icons.USER);
        FilledButtonWithIcon fbIconFactoryC = FilledButton.icon("FB Icon C", Icons.GEAR, Icons.ARROW_RIGHT);

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
        fwi.setLabelText("setLabelText");

        // === 5) ทดสอบ StyleMasker ของ FilledButtonWithIcon กับ Button ปกติ ===
        Button plainBtnToIconMask = new Button("MaskWithIcon");
        // ใส่ไอคอนซ้าย/ขวาตอน mask
        FilledButtonWithIcon.mask(plainBtnToIconMask, Icons.ARROW_LEFT, Icons.ARROW_RIGHT);

        // === 6) ทดสอบ replace / from / fromAndReplace ===
        // 6.1) replaceInParentOf(...) : นำ fwi ไปแทนที่ buttonInHBox
        fwi.replaceInParentOf(buttonInHBoxFilled);

        // 6.2) from(...) : clone properties จาก buttonInVBox -> ได้เป็น FilledButton
        FilledButton fbFromVBox = FilledButton.from(buttonInVBoxFilled);
        // แล้วค่อย replace เข้าไปแทนของเก่า
        fbFromVBox.replaceInParentOf(buttonInVBoxFilled);


        // === 7) เพิ่มของให้เห็นภาพรวมใน HBox ทดลอง ===
        //   - loneIcon
        //   - fbDefault / fbSmallEmpty / fbSmall
        //   - fbIconFactoryA/B/C (จาก factory icon)
        //   - maskedBtnMedium / maskedBtnSmall (masker)
        //   - plainBtnToIconMask (with-icon mask)
        // หมายเหตุ: fwi ถูก replace ไปแทน buttonInHBox แล้ว จึงไม่ต้อง add ลง parentVBox ซ้ำ
        fbIconFactoryB.setPrefWidth(140);
        parentVBoxFilled.getChildren().addAll(
                fbDefault, fbSmallEmpty, fbSmall, fwiDefault, fwiSmall, fbiMedium,
                fbIconFactoryA, fbIconFactoryB, fbIconFactoryC,
                maskedBtnMedium, maskedBtnSmall,
                plainBtnToIconMask
        );

        // === 8) ตรวจ getters เพิ่มเติมให้ครบ (log) ===
        System.out.println("[fbDefault] styleClass=" + fbDefault.getStyleClass());
        System.out.println("[fbIconFactoryA] class=" + fbIconFactoryA.getClass().getSimpleName());
        System.out.println("[plainBtnToIconMask] styleClass=" + plainBtnToIconMask.getStyleClass());

        // === 0) เตรียมพื้นที่แสดงผล ===
        parentVBoxElevated.setSpacing(4);

        ElevatedButton ebDefault = new ElevatedButton("ElevatedButton");

        // small() แบบไม่มี/มี label
        ElevatedButton ebSmallEmpty = ElevatedButton.small();
        ElevatedButton ebSmall = ElevatedButton.small("Small EB");

        // factory: icon(...) จะได้ ElevatedButtonWithIcon (subclass)
        ElevatedButtonWithIcon ebIconFactoryA = ElevatedButton.icon("EB Icon A");
        ElevatedButtonWithIcon ebIconFactoryB = ElevatedButton.icon("EB Icon B", Icons.USER);
        ElevatedButtonWithIcon ebIconFactoryC = ElevatedButton.icon("EB Icon C", Icons.GEAR, Icons.ARROW_RIGHT);

        // mask แบบ MEDIUM กับปุ่มธรรมดา
        Button elevatedMaskedBtnMedium = new Button("Masked MEDIUM");
        ElevatedButton.mask(elevatedMaskedBtnMedium); // = MEDIUM.mask

        // mask แบบ SMALL กับปุ่มธรรมดา
        Button elevatedMaskedBtnSmall = new Button("Masked SMALL");
        ElevatedButton.SMALL.mask(elevatedMaskedBtnSmall);

        // === Default ===
        ElevatedButtonWithIcon ewiDefault = new ElevatedButtonWithIcon("Default EWI", Icons.USER);

        // === Small ===
        ElevatedButtonWithIcon ewiSmall = ElevatedButtonWithIcon.small("Small EWI", Icons.GEAR, Icons.ARROW_RIGHT);

        ElevatedButtonWithIcon ebiMedium = ElevatedButtonWithIcon.medium("Small EWI", Icons.GEAR, Icons.ARROW_RIGHT);

        // === Medium ===
        Button elevatedMediumBtn = new Button("Medium masked");
        ElevatedButton.MEDIUM.mask(elevatedMediumBtn);

        ElevatedButton ebMedium = new ElevatedButton("Medium EB");
        ebMedium.getStyleClass().add("medium");

        ElevatedButtonWithIcon ewi = new ElevatedButtonWithIcon("WithIcon", Icons.EYE, Icons.ARROW_RIGHT);
        ewi.setLabelText("Confirm");

        ewi.setIconLeft(Icons.SMILEY);
        ewi.setIconRight(Icons.USER);

        ewi.setButtonSize(Region.USE_COMPUTED_SIZE, 40);
        ewi.setButtonHeight(40);

        ewi.setSpaceLeft(8);
        ewi.setSpaceRight(12);

        ewi.setIconSize(22);
        ewi.setLeftIconSize(20);
        ewi.setRightIconSize(24);

        ewi.setDisable(true);

        ewi.setTextLabel(new javafx.scene.control.Label("รีแบรนด์ใหม่"));
        ewi.setLabelText("setLabelText");

        Button elevatedPlainBtnToIconMask = new Button("MaskWithIcon");
        ElevatedButtonWithIcon.mask(elevatedPlainBtnToIconMask, Icons.ARROW_LEFT, Icons.ARROW_RIGHT);

        ewi.replaceInParentOf(buttonInHBoxElevated);

        ElevatedButton ebFromVBox = ElevatedButton.from(buttonInVBoxElevated);
        ebFromVBox.replaceInParentOf(buttonInVBoxElevated);

        ebIconFactoryB.setPrefWidth(140);
        parentVBoxElevated.getChildren().addAll(
                ebDefault, ebSmallEmpty, ebSmall, ewiDefault, ewiSmall, ebiMedium,
                ebIconFactoryA, ebIconFactoryB, ebIconFactoryC,
                elevatedMaskedBtnMedium, elevatedMaskedBtnSmall,
                elevatedPlainBtnToIconMask
        );

        parentVBoxOutlined.setSpacing(4);

        OutlinedButton obDefault = new OutlinedButton("OutlinedButton");

    // small() แบบไม่มี/มี label
        OutlinedButton obSmallEmpty = OutlinedButton.small();
        OutlinedButton obSmall = OutlinedButton.small("Small OB");

// factory: icon(...) จะได้ OutlinedButtonWithIcon (subclass)
        OutlinedButtonWithIcon obIconFactoryA = OutlinedButton.icon("OB Icon A");
        OutlinedButtonWithIcon obIconFactoryB = OutlinedButton.icon("OB Icon B", Icons.USER);
        OutlinedButtonWithIcon obIconFactoryC = OutlinedButton.icon("OB Icon C", Icons.GEAR, Icons.ARROW_RIGHT);

// mask แบบ MEDIUM กับปุ่มธรรมดา
        Button outlinedMaskedBtnMedium = new Button("Masked MEDIUM");
        OutlinedButton.mask(outlinedMaskedBtnMedium); // = MEDIUM.mask

// mask แบบ SMALL กับปุ่มธรรมดา
        Button outlinedMaskedBtnSmall = new Button("Masked SMALL");
        OutlinedButton.SMALL.mask(outlinedMaskedBtnSmall);

// === Default ===
        OutlinedButtonWithIcon owiDefault = new OutlinedButtonWithIcon("Default EWI", Icons.USER);

// === Small ===
        OutlinedButtonWithIcon owiSmall = OutlinedButtonWithIcon.small("Small EWI", Icons.GEAR, Icons.ARROW_RIGHT);

        OutlinedButtonWithIcon obiMedium = OutlinedButtonWithIcon.medium("Small EWI", Icons.GEAR, Icons.ARROW_RIGHT);

// === Medium ===
        Button outlinedMediumBtn = new Button("Medium masked");
        OutlinedButton.MEDIUM.mask(outlinedMediumBtn);

        OutlinedButton obMedium = new OutlinedButton("Medium OB");
        obMedium.getStyleClass().add("medium");

        OutlinedButtonWithIcon owi = new OutlinedButtonWithIcon("WithIcon", Icons.EYE, Icons.ARROW_RIGHT);
        owi.setLabelText("Confirm");

        owi.setIconLeft(Icons.SMILEY);
        owi.setIconRight(Icons.USER);

        owi.setButtonSize(Region.USE_COMPUTED_SIZE, 40);
        owi.setButtonHeight(40);

        owi.setSpaceLeft(8);
        owi.setSpaceRight(12);

        owi.setIconSize(22);
        owi.setLeftIconSize(20);
        owi.setRightIconSize(24);

        owi.setDisable(true);

        owi.setTextLabel(new javafx.scene.control.Label("รีแบรนด์ใหม่"));
        owi.setLabelText("setLabelText");

        Button outlinedPlainBtnToIconMask = new Button("MaskWithIcon");
        OutlinedButtonWithIcon.mask(outlinedPlainBtnToIconMask, Icons.ARROW_LEFT, Icons.ARROW_RIGHT);

        owi.replaceInParentOf(buttonInHBoxOutlined);

        OutlinedButton obFromVBox = OutlinedButton.from(buttonInVBoxOutlined);
        obFromVBox.replaceInParentOf(buttonInVBoxOutlined);

        obIconFactoryB.setPrefWidth(140);
        parentVBoxOutlined.getChildren().addAll(
                obDefault, obSmallEmpty, obSmall, owiDefault, owiSmall, obiMedium,
                obIconFactoryA, obIconFactoryB, obIconFactoryC,
                outlinedMaskedBtnMedium, outlinedMaskedBtnSmall,
                outlinedPlainBtnToIconMask
        );

    }

}
