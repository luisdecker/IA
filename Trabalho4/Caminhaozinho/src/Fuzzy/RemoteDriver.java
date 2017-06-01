package remoteDriver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.StringTokenizer;
import net.sourceforge.jFuzzyLogic.FIS;

public class RemoteDriver {

    static int port = 4322;
    
    static String host = "localhost";
    //PASSE O ARGUENTO "s" PARA SIMULAR O CAMINHONEIRO BEBADO!
    public static void main(String[] args) throws IOException, InterruptedException {
        if(args.length > 0 && args[0].charAt(0)=='s'){
            port = 4321;
        }
        String filename = "src/Fuzzy/ControladorCaminhao.fcl";
        File f = new File(filename);
        if (f.exists() && !f.isDirectory()) {
            System.out.println("ACHOU O ARQUIVO DE CONFIGURACOES!");
        }
        String diretorio = System.getProperty("user.dir");
        System.out.println("PASTA ATUAL: " + diretorio);

        FIS logica = FIS.load(filename, true);
        if (logica == null) {
            System.err.println("Não foi possível carregar o arquivo de configurações da lógica fuzzy!\n" + filename);
            return;
        }
        Socket kkSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            kkSocket = new Socket(host, port);
            out = new PrintWriter(kkSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + host);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + host);
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;

        double x, y;
        double angle;
        Scanner scanner = new Scanner(System.in);
        // requisicao da posicao do caminhao
        out.println("r");
        //logica.chart();
        //Lendo as configurações da logica fuzzy
        while ((fromServer = in.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(fromServer);
            x = Double.valueOf(st.nextToken()).doubleValue();
            y = Double.valueOf(st.nextToken()).doubleValue();
            angle = Double.valueOf(st.nextToken()).doubleValue();

            System.out.println("x: " + x + " y: " + y + " angle: " + angle);

            /////////////////////////////////////////////////////////////////////////////////////
            // TODO sua lógica fuzzy vai aqui use os valores de x,y e angle obtidos. x e y estao em [0,1] e angulo [0,360)
            System.out.println("Antes da Fuzzy");
            logica.setVariable("posicaoX", x);
            logica.setVariable("posicaoY", y);
            logica.setVariable("anguloOlhando", angle);
            
            logica.evaluate();
            //logica.getVariable("volante").chartDefuzzifier(true);
           // logica.chart();
            
            
            
            double respostaDaSuaLogica = logica.getVariable("volante").getLatestDefuzzifiedValue(); // atribuir um valor entre -1 e 1 para virar o volante pra esquerda ou direita.
            System.out.println("A logica respondeu :" + respostaDaSuaLogica);
            ///////////////////////////////////////////////////////////////////////////////// Acaba sua modificacao aqui
            // envio da acao do volante
            if(args.length > 0 && args[0].charAt(0)=='s'){
                respostaDaSuaLogica += ((Math.random()*2) -1)*0.5;
            }
            out.println(respostaDaSuaLogica);

            // requisicao da posicao do caminhao        
            
            out.println("r");
            //scanner.nextLine();
        }
        
        out.close();
        in.close();
        stdIn.close();
        kkSocket.close();
    }
}
