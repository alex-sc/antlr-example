import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Python3Demo {
    private static void listFiles(File folder, List<File> accumulator) {
        for (File file : folder.listFiles()) {
            if (file.isFile()) {
                if (file.getName().toLowerCase().endsWith(".py")) {
                    accumulator.add(file);
                }
            } else {
                listFiles(file, accumulator);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        List<File> accumulator = new ArrayList<>();
        listFiles(new File("D:/pdf/pyHanko-master"), accumulator);

        long before = System.currentTimeMillis();
        for (File file : accumulator) {
            ANTLRInputStream antlrInputStream = new ANTLRInputStream(new FileInputStream(file));

            Python3Lexer lexer = new Python3Lexer(antlrInputStream);
            CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            Python3Parser parser = new Python3Parser(tokenStream);
            ParseTree tree = parser.file_input();
            ParseTreeWalker walker = new ParseTreeWalker();
            walker.walk(new ImportListener(), tree);
        }
        System.err.println("Scanned " + accumulator.size() + " files in " + (System.currentTimeMillis() - before) / 1000 + " seconds");
    }

    private static class ImportListener extends Python3ParserBaseListener {
        private final List<String> packageNames;

        private ImportListener() {
            packageNames = new ArrayList<>();
            packageNames.add("cryptography");
        }

        @Override
        public void enterImport_stmt(Python3Parser.Import_stmtContext ctx){
            Python3Parser.Import_fromContext from = ctx.import_from();
            Python3Parser.Import_nameContext name = ctx.import_name();
            if (from == null && name == null) {
                return;
            }
            String packageName;
            if (from != null && from.dotted_name() != null) {
                packageName = from.dotted_name().getText();
            } else if (name != null && name.dotted_as_names() != null) {
                packageName = name.dotted_as_names().getText();
            } else {
                return;
            }
            //System.err.println(packageName);
            packageNames.forEach(pkg -> {
                if (packageName.startsWith(pkg)) {
                    System.out.println("Package name detected: " + pkg);
                }
            });
        }
    }
}
