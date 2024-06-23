import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AntlrDemo {
    public static void main(String[] args) throws IOException {
        ANTLRInputStream antlrInputStream = new ANTLRInputStream(new FileInputStream(new File("src/main/java/example/BouncyCastleDemo.java")));

        Java8Lexer lexer = new Java8Lexer(antlrInputStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        Java8Parser parser = new Java8Parser(tokenStream);
        ParseTree tree = parser.compilationUnit();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new ImportListener(), tree);
    }
    private static class ImportListener extends Java8BaseListener {
        private final List<String> packageNames;

        private ImportListener() {
            packageNames = new ArrayList<>();
            packageNames.add("org.bouncycastle");
        }

        @Override
        public void enterImportDeclaration(Java8Parser.ImportDeclarationContext ctx) {
            String packageName = getPackage(ctx.singleTypeImportDeclaration().typeName());
            packageNames.forEach(pkg -> {
                if (packageName.startsWith(pkg)) {
                    System.out.println("Package name detected: " + pkg);
                }
            });
        }

        private String getPackage(Java8Parser.TypeNameContext type) {
            return type.packageOrTypeName().packageOrTypeName().getText();
        }
    }
}
