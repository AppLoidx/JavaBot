package core.modules.tesseract;

import net.sourceforge.tess4j.TesseractException;

import java.io.File;

/**
 * @author Arthur Kupriyanov
 */
public class Tesseract {
    private String dataPath = "src/main/resources/tesseract";
    public Tesseract setDataPath(String pathname){
        dataPath = pathname;
        return this;
    }
    public String getEngText(File file){
        net.sourceforge.tess4j.Tesseract tess = new net.sourceforge.tess4j.Tesseract();
        tess.setDatapath(dataPath);
        tess.setLanguage("eng");
        String fullText = null;
        try {
            fullText = tess.doOCR(file);
        } catch (TesseractException e) {
            e.printStackTrace();
        }

        return fullText;
    }
}
