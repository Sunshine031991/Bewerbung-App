package com.bewerbung;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BewerbungsApp extends Application {

    // Farben für Dark Mode Theme
    private static final String BACKGROUND_COLOR = "#0a0e27";
    private static final String CARD_COLOR = "#1a1f3a";
    private static final String ACCENT_COLOR = "#00d4ff";
    private static final String TEXT_COLOR = "#e0e0e0";
    private static final String SECONDARY_TEXT = "#a0a0a0";
    private BorderPane mainLayout;
    private StackPane contentArea;
    private String currentPage = "home";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Bewerbungs-Portfolio");

        mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");

        // Navigation erstellen
        VBox navigation = createNavigation();
        mainLayout.setLeft(navigation);

        // Content-Bereich
        contentArea = new StackPane();
        contentArea.setPadding(new Insets(30));
        mainLayout.setCenter(contentArea);

        // Startseite anzeigen
        showHomePage();

        Scene scene = new Scene(mainLayout, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createNavigation() {
        VBox nav = new VBox(15);
        nav.setPadding(new Insets(30, 20, 30, 20));
        nav.setStyle("-fx-background-color: " + CARD_COLOR + ";");
        nav.setPrefWidth(250);

        Label title = new Label("PORTFOLIO");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));
        title.setTextFill(Color.web(ACCENT_COLOR));
        title.setPadding(new Insets(0, 0, 30, 0));

        Button btnHome = createNavButton("🏠 Start Page", "home");
        Button btnPersonal = createNavButton("👤 Personal Data", "personal");
        Button btnCV = createNavButton("📄 CV", "cv");
        Button btnSkills = createNavButton("⚡ Skills", "skills");
        Button btnProjects = createNavButton("💼 Projects", "projects");
        Button btnContact = createNavButton("📧 Contact", "contact");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button btnExportPDF = createActionButton("📥 PDF Export");
        btnExportPDF.setOnAction(e -> exportToPDF());

        nav.getChildren().addAll(title, btnHome, btnPersonal, btnCV, btnSkills,
                btnProjects, btnContact, spacer, btnExportPDF);

        return nav;
    }

    private Button createNavButton(String text, String page) {
        Button btn = new Button(text);
        btn.setPrefWidth(210);
        btn.setPrefHeight(45);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setFont(Font.font("System", 14));

        updateButtonStyle(btn, page.equals(currentPage));

        btn.setOnAction(e -> {
            currentPage = page;
            updateAllButtonStyles();
            switchPage(page);
        });

        btn.setOnMouseEntered(e -> {
            if (!page.equals(currentPage)) {
                btn.setStyle(getButtonHoverStyle());
            }
        });

        btn.setOnMouseExited(e -> {
            updateButtonStyle(btn, page.equals(currentPage));
        });

        return btn;
    }

    private void updateButtonStyle(Button btn, boolean isActive) {
        if (isActive) {
            btn.setStyle(getButtonActiveStyle());
        } else {
            btn.setStyle(getButtonDefaultStyle());
        }
    }

    private String getButtonDefaultStyle() {
        return "-fx-background-color: transparent; " +
                "-fx-text-fill: " + TEXT_COLOR + "; " +
                "-fx-background-radius: 8; " +
                "-fx-cursor: hand;";
    }

    private String getButtonHoverStyle() {
        return "-fx-background-color: rgba(0, 212, 255, 0.1); " +
                "-fx-text-fill: " + ACCENT_COLOR + "; " +
                "-fx-background-radius: 8; " +
                "-fx-cursor: hand;";
    }

    private String getButtonActiveStyle() {
        return "-fx-background-color: " + ACCENT_COLOR + "; " +
                "-fx-text-fill: " + BACKGROUND_COLOR + "; " +
                "-fx-background-radius: 8; " +
                "-fx-font-weight: bold;";
    }

    private Button createActionButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(210);
        btn.setPrefHeight(45);
        btn.setFont(Font.font("System", FontWeight.BOLD, 14));
        btn.setStyle("-fx-background-color: " + ACCENT_COLOR + "; " +
                "-fx-text-fill: " + BACKGROUND_COLOR + "; " +
                "-fx-background-radius: 8; " +
                "-fx-cursor: hand;");

        btn.setOnMouseEntered(e -> {
            btn.setStyle("-fx-background-color: #00b8e6; " +
                    "-fx-text-fill: " + BACKGROUND_COLOR + "; " +
                    "-fx-background-radius: 8; " +
                    "-fx-cursor: hand;");
        });

        btn.setOnMouseExited(e -> {
            btn.setStyle("-fx-background-color: " + ACCENT_COLOR + "; " +
                    "-fx-text-fill: " + BACKGROUND_COLOR + "; " +
                    "-fx-background-radius: 8; " +
                    "-fx-cursor: hand;");
        });

        return btn;
    }

    private void updateAllButtonStyles() {
        // Diese Methode wird aufgerufen, um alle Buttons zu aktualisieren
    }

    private void switchPage(String page) {
        switch (page) {
            case "home" -> showHomePage();
            case "personal" -> showPersonalPage();
            case "cv" -> showCVPage();
            case "skills" -> showSkillsPage();
            case "projects" -> showProjectsPage();
            case "contact" -> showContactPage();
        }
    }

    private void showHomePage() {
        VBox content = new VBox(30);
        content.setAlignment(Pos.CENTER);
        content.setMaxWidth(800);

        Label welcome = new Label("Welcome to my interactive application!");
        welcome.setFont(Font.font("System", FontWeight.BOLD, 48));
        welcome.setTextFill(Color.web(ACCENT_COLOR));

        Label subtitle = new Label("My professional application");
        subtitle.setFont(Font.font("System", 24));
        subtitle.setTextFill(Color.web(TEXT_COLOR));

        Label description = new Label(
                """
                This interactive application app showcases my qualifications,
                Experience and projects in a modern and innovative way.
                
                Use the navigation on the left to learn more about me.""");
        description.setFont(Font.font("System", 16));
        description.setTextFill(Color.web(SECONDARY_TEXT));
        description.setStyle("-fx-text-alignment: center;");
        description.setWrapText(true);
        description.setMaxWidth(600);

        VBox card = createCard();
        card.getChildren().addAll(welcome, subtitle, description);
        content.getChildren().add(card);

        setContent(content);
    }

    private void showPersonalPage() {
        VBox content = new VBox(20);
        content.setMaxWidth(800);

        Label title = createTitle("Personal Data");

        VBox card = createCard();
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));

        // Beispieldaten - hier kannst du deine echten Daten einfügen
        addDataRow(grid, 0, "Name:", "Steven Dias Carrilho");
        addDataRow(grid, 1, "Birthday:", "08.03.1991");
        addDataRow(grid, 2, "Nationality:", "Portugal");
        addDataRow(grid, 3, "Residence:", "Soutosa, Moimenta da Beira, Portugal");
        addDataRow(grid, 4, "E-Mail:", "steven.dias.carrilho@gmail.com");
        addDataRow(grid, 5, "Phone:", "928044197");

        card.getChildren().add(grid);
        content.getChildren().addAll(title, card);

        setContent(content);
    }

    private void showCVPage() {
        VBox content = new VBox(20);
        content.setMaxWidth(800);

        Label title = createTitle("CV & Career History");

        VBox timeline = new VBox(15);

        timeline.getChildren().addAll(
                createTimelineItem("2022 - Now", "1stL Support for Zeiss",
                        "FujitsuPT", "First Line Support, Trainer for Newcomers"),
                createTimelineItem("2022 - 2022", "1stL Support for Philips HUE",
                        "Webhelp", "Customer support for smart home infrastructure"),
                createTimelineItem("2022 - 2013", "Waiter in England, Portugal, Italy, Switzerland ",
                        "Various Hotels", "Various Restaurants") );

        content.getChildren().addAll(title, timeline);
        setContent(content);
    }

    private void showSkillsPage() {
        VBox content = new VBox(20);
        content.setMaxWidth(800);

        Label title = createTitle("Skills & Knowledge");

        VBox skillsBox = new VBox(15);

        skillsBox.getChildren().addAll(
                createSkillBar("Java", 45),
                createSkillBar("JavaFX", 25),
                createSkillBar("Python", 60),
                createSkillBar("Rust", 30),
                createSkillBar("Maven / Gradle", 20),
                createSkillBar("REST APIs", 0),
                createSkillBar("Cyber Security", 75),
                createSkillBar("Windows Admin", 40),
                createSkillBar("Debuging", 35)
        );

        content.getChildren().addAll(title, skillsBox);
        setContent(content);
    }

    private void showProjectsPage() {
        VBox content = new VBox(20);
        content.setMaxWidth(800);

        Label title = createTitle("Projects & Portfolio");

        VBox projectsBox = new VBox(15);

        projectsBox.getChildren().addAll(
                createProjectCard("These App for Candidation.",
                        "This application was developed as part of my job application process. It serves as an interactive portfolio to showcase my qualifications, experience and projects in a modern and innovative way. The app is built using JavaFX and features a dark mode theme, smooth animations and a user-friendly interface.",
                        "Java, JavaFX, UI/UX Design") );

        content.getChildren().addAll(title, projectsBox);
        setContent(content);
    }

    private void showContactPage() {
        VBox content = new VBox(20);
        content.setMaxWidth(800);

        Label title = createTitle("Contact & Links");

        VBox card = createCard();
        VBox contactBox = new VBox(20);
        contactBox.setPadding(new Insets(20));

        Label info = new Label("Please feel free to contact me via the following channels:");
        info.setFont(Font.font("System", 16));
        info.setTextFill(Color.web(TEXT_COLOR));

        // E-Mail Button
        Button emailBtn = createLinkButton("📧 Send E-Mail", "steven.carrilho@fujitsu.com");

        // GitHub Button
        Button githubBtn = createLinkButton("🔗 GitHub Profile", "https://github.com/Sunshine031991");

        // LinkedIn Button
        Button linkedinBtn = createLinkButton("🔗 LinkedIn Profile", "https://www.linkedin.com/in/steven-carrilho-923651175/");

        contactBox.getChildren().addAll(info, emailBtn, githubBtn, linkedinBtn);
        card.getChildren().add(contactBox);
        content.getChildren().addAll(title, card);

        setContent(content);
    }

    private Button createLinkButton(String text, String url) {
        Button btn = new Button(text);
        btn.setPrefWidth(300);
        btn.setPrefHeight(50);
        btn.setFont(Font.font("System", 16));
        btn.setStyle("-fx-background-color: " + CARD_COLOR + "; " +
                "-fx-text-fill: " + ACCENT_COLOR + "; " +
                "-fx-border-color: " + ACCENT_COLOR + "; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 8; " +
                "-fx-background-radius: 8; " +
                "-fx-cursor: hand;");

        btn.setOnAction(e -> openURL(url));

        btn.setOnMouseEntered(e -> {
            btn.setStyle("-fx-background-color: " + ACCENT_COLOR + "; " +
                    "-fx-text-fill: " + BACKGROUND_COLOR + "; " +
                    "-fx-border-color: " + ACCENT_COLOR + "; " +
                    "-fx-border-width: 2; " +
                    "-fx-border-radius: 8; " +
                    "-fx-background-radius: 8; " +
                    "-fx-cursor: hand;");
        });

        btn.setOnMouseExited(e -> {
            btn.setStyle("-fx-background-color: " + CARD_COLOR + "; " +
                    "-fx-text-fill: " + ACCENT_COLOR + "; " +
                    "-fx-border-color: " + ACCENT_COLOR + "; " +
                    "-fx-border-width: 2; " +
                    "-fx-border-radius: 8; " +
                    "-fx-background-radius: 8; " +
                    "-fx-cursor: hand;");
        });

        return btn;
    }

    private void openURL(String url) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                desktop.browse(new URI(url));
            }
        } catch (IOException | URISyntaxException e) {
            showAlert("Error", "Link could not be opened: " + url);
        }
    }

    private VBox createCard() {
        VBox card = new VBox();
        card.setStyle("-fx-background-color: " + CARD_COLOR + "; " +
                "-fx-background-radius: 15; " +
                "-fx-padding: 30;");

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 212, 255, 0.3));
        shadow.setRadius(20);
        card.setEffect(shadow);

        return card;
    }

    private Label createTitle(String text) {
        Label title = new Label(text);
        title.setFont(Font.font("System", FontWeight.BOLD, 36));
        title.setTextFill(Color.web(ACCENT_COLOR));
        return title;
    }

    private void addDataRow(GridPane grid, int row, String label, String value) {
        Label lblLabel = new Label(label);
        lblLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        lblLabel.setTextFill(Color.web(SECONDARY_TEXT));

        Label lblValue = new Label(value);
        lblValue.setFont(Font.font("System", 14));
        lblValue.setTextFill(Color.web(TEXT_COLOR));

        grid.add(lblLabel, 0, row);
        grid.add(lblValue, 1, row);
    }

    private VBox createTimelineItem(String period, String title, String company, String description) {
        VBox item = createCard();
        item.setSpacing(10);

        Label lblPeriod = new Label(period);
        lblPeriod.setFont(Font.font("System", FontWeight.BOLD, 12));
        lblPeriod.setTextFill(Color.web(ACCENT_COLOR));

        Label lblTitle = new Label(title);
        lblTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        lblTitle.setTextFill(Color.web(TEXT_COLOR));

        Label lblCompany = new Label(company);
        lblCompany.setFont(Font.font("System", FontWeight.BOLD, 14));
        lblCompany.setTextFill(Color.web(SECONDARY_TEXT));

        Label lblDesc = new Label(description);
        lblDesc.setFont(Font.font("System", 12));
        lblDesc.setTextFill(Color.web(SECONDARY_TEXT));
        lblDesc.setWrapText(true);

        item.getChildren().addAll(lblPeriod, lblTitle, lblCompany, lblDesc);
        return item;
    }

    private VBox createSkillBar(String skillName, int percentage) {
        VBox skillBox = new VBox(5);

        HBox labelBox = new HBox();
        labelBox.setAlignment(Pos.CENTER_LEFT);
        Label lblSkill = new Label(skillName);
        lblSkill.setFont(Font.font("System", 14));
        lblSkill.setTextFill(Color.web(TEXT_COLOR));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lblPercent = new Label(percentage + "%");
        lblPercent.setFont(Font.font("System", FontWeight.BOLD, 14));
        lblPercent.setTextFill(Color.web(ACCENT_COLOR));

        labelBox.getChildren().addAll(lblSkill, spacer, lblPercent);

        ProgressBar bar = new ProgressBar(percentage / 100.0);
        bar.setPrefWidth(Double.MAX_VALUE);
        bar.setPrefHeight(20);
        bar.setStyle("-fx-accent: " + ACCENT_COLOR + ";");

        skillBox.getChildren().addAll(labelBox, bar);
        return skillBox;
    }

    private VBox createProjectCard(String title, String description, String technologies) {
        VBox card = createCard();
        card.setSpacing(10);

        Label lblTitle = new Label(title);
        lblTitle.setFont(Font.font("System", FontWeight.BOLD, 20));
        lblTitle.setTextFill(Color.web(TEXT_COLOR));

        Label lblDesc = new Label(description);
        lblDesc.setFont(Font.font("System", 14));
        lblDesc.setTextFill(Color.web(SECONDARY_TEXT));
        lblDesc.setWrapText(true);

        Label lblTech = new Label("Technologien: " + technologies);
        lblTech.setFont(Font.font("System", FontWeight.BOLD, 12));
        lblTech.setTextFill(Color.web(ACCENT_COLOR));

        card.getChildren().addAll(lblTitle, lblDesc, lblTech);
        return card;
    }

    private void setContent(VBox content) {
        contentArea.getChildren().clear();

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: " + BACKGROUND_COLOR + "; " +
                "-fx-background-color: transparent;");
        scrollPane.getStyleClass().add("edge-to-edge");

        // Fade-In Animation
        FadeTransition fade = new FadeTransition(Duration.millis(300), scrollPane);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();

        contentArea.getChildren().add(scrollPane);
    }

    private void exportToPDF() {
        showAlert("PDF Export", """
                                PDF-Export-Funktion w\u00fcrde hier die Bewerbung als PDF speichern.
                                
                                F\u00fcr eine vollst\u00e4ndige Implementierung wird die iText-Bibliothek ben\u00f6tigt.""");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: " + CARD_COLOR + ";");
        dialogPane.lookup(".content.label").setStyle("-fx-text-fill: " + TEXT_COLOR + ";");

        alert.showAndWait();
    }
}
