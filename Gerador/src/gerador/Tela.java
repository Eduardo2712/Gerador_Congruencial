/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerador;

import com.sun.javafx.geom.AreaOp;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Eduardo Manoel de Lara
 */
public class Tela extends javax.swing.JFrame {

    double tempo;
    int TOTALN = 999999;
    List<Double> lista = new ArrayList<>();
    double fator[] = new double[6];

    public Tela() {
        initComponents();
    }

    public void fatorial() {
        fator[0] = 2;
        fator[1] = 6;
        fator[2] = 24;
        fator[3] = 120;
        fator[4] = 720;
        fator[5] = 5040;
    }

    public void geraArquivo(double n, double m, double a, double c, String nome) {
        lista.clear();
        int i = 0;
        double aux;
        tempo = System.currentTimeMillis();
        while (i < TOTALN) {
            aux = (((a * n) + c) % m);
            lista.add(aux / (m - 1));
            n = aux;
            System.out.println("" + (i + 1) + " -> " + lista.get(i));
            i += 1;
        }
        tempo = System.currentTimeMillis() - tempo;
        tempoGasto.setText("Tempo gasto: " + tempo + " milissegundos");
        String nomeArquivo = nome;
        File arquivo = new File(nomeArquivo);
        if (arquivo.exists() == true) {
            arquivo.delete();
        }
        try {
            arquivo.createNewFile();
            FileWriter fw = new FileWriter(arquivo.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            i = 0;
            while (i < TOTALN) {
                bw.write("" + lista.get(i) + "\n");
                i++;
            }
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void k_S_Gera() {
        DefaultTableModel modelo = (DefaultTableModel) tabelaK_S.getModel();
        modelo.setNumRows(0);
        double auxLi = 0, auxLs = 0.1, antGx = 0, antFx = 0, maiorFx_Gx = 0;
        int i = 0, j = 0;
        DecimalFormat df = new DecimalFormat("0.0");
        DecimalFormat df2 = new DecimalFormat("0.000");
        while (i < 10) {
            K_S k_s = new K_S();
            k_s.li = auxLi;
            k_s.ls = auxLs;
            int contN = 0;
            while (j < TOTALN) {
                if (lista.get(j) >= k_s.li && lista.get(j) < k_s.ls) {
                    contN += 1;
                }
                j++;
            }
            k_s.fo = contN;
            k_s.fe = TOTALN / 10;
            k_s.fo_fe = (Math.pow((k_s.fo - k_s.fe), 2)) / k_s.fe;
            k_s.pi = k_s.fo / TOTALN;
            k_s.gx = k_s.pi + antGx;
            k_s.fx = antFx + 0.1;
            k_s.fx_gx = Math.abs(k_s.fx - k_s.gx);
            if (k_s.fx_gx > maiorFx_Gx) {
                maiorFx_Gx = k_s.fx_gx;
            }
            modelo.addRow(new Object[]{
                df2.format(k_s.li),
                df2.format(k_s.ls),
                df2.format(k_s.fo),
                df2.format(k_s.fe),
                df2.format(k_s.fo_fe),
                df2.format(k_s.pi),
                df2.format(k_s.gx),
                df2.format(k_s.fx),
                df2.format(k_s.fx_gx)
            }
            );
            auxLi += 0.1;
            auxLs += 0.1;
            antGx = k_s.gx;
            antFx = k_s.fx;
            contN = 0;
            j = 0;
            i++;
        }
        kSCalcUni.setText("K_S: " + df2.format(maiorFx_Gx));
        kS5Uni.setText("K_S 5%: " + df2.format((1.36 / Math.sqrt(TOTALN))));
        if (maiorFx_Gx < (1.36 / Math.sqrt(TOTALN))) {
            aceitaUni.setText("Aceita H0, valores tem dist. uniforme");
        } else {
            aceitaUni.setText("Rejeita H0, valores não tem dist. uniforme");
        }
    }

    public void corridaAsce() {
        DefaultTableModel modelo = (DefaultTableModel) tabelaCorridaAsc.getModel();
        modelo.setNumRows(0);
        DecimalFormat df = new DecimalFormat("0.0");
        DecimalFormat df2 = new DecimalFormat("0.000");
        double corrida[] = new double[6];
        int i = 1;
        int posVetor = 0;
        double maior = lista.get(0);
        while (i <= TOTALN) {
            if (i < TOTALN) {
                if (maior > lista.get(i)) {
                    maior = lista.get(i);
                    i += 1;
                    posVetor += 1;
                } else {
                    i += 1;
                    if (i < TOTALN) {
                        maior = lista.get(i);
                        i += 1;
                        if (posVetor < 6) {
                            corrida[posVetor] += 1;
                        }
                    } else {
                        corrida[posVetor] += 1;
                    }
                    posVetor = 0;
                }
            } else {
                corrida[posVetor] += 1;
                i += 1;
            }
        }
        i = 0;
        double somaFo = 0;
        double somaGx = 0;
        double somaFx = 0;
        double maiorGx_Fx = 0;
        while (i < 6) {
            somaFo += corrida[i];
            i++;
        }
        i = 0;
        while (i < 6) {
            somaGx += (corrida[i] / somaFo);
            somaFx += ((i + 1) / fator[i]);
            if (maiorGx_Fx < Math.abs(somaGx - somaFx)) {
                maiorGx_Fx = Math.abs(somaGx - somaFx);
            }
            double aux = (corrida[i] / somaFo);
            double aux2 = ((i + 1) / fator[i]);
            modelo.addRow(new Object[]{
                (i + 1),
                corrida[i],
                df2.format(aux),
                df2.format(somaGx),
                df2.format(aux2),
                df2.format(somaFx),
                df2.format(Math.abs(somaGx - somaFx))
            }
            );
            i++;
        }
        kSCalcCorridaAsc.setText("K_S: " + df2.format(maiorGx_Fx));
        kSCorridaAsc.setText("K_S 5%: " + df2.format((1.36 / Math.sqrt(somaFo))));
        if (maiorGx_Fx < (1.36 / Math.sqrt(somaFo))) {
            aceitaCorridaAsc.setText("Aceita H0, corridas não diferem das esperadas");
        } else {
            aceitaCorridaAsc.setText("Rejeita H0, corridas diferem das esperadas");
        }
    }

    public void corridaDesc() {
        DefaultTableModel modelo = (DefaultTableModel) tabelaCorridaDes.getModel();
        modelo.setNumRows(0);
        DecimalFormat df = new DecimalFormat("0.0");
        DecimalFormat df2 = new DecimalFormat("0.000");
        double corrida[] = new double[6];
        int i = 1;
        int posVetor = 0;
        double maior = lista.get(0);
        while (i <= TOTALN) {
            if (i < TOTALN) {
                if (maior < lista.get(i)) {
                    maior = lista.get(i);
                    i += 1;
                    posVetor += 1;
                } else {
                    i += 1;
                    if (i < TOTALN) {
                        maior = lista.get(i);
                        i += 1;
                        if (posVetor < 6) {
                            corrida[posVetor] += 1;
                        }
                    } else {
                        corrida[posVetor] += 1;
                    }
                    posVetor = 0;
                }
            } else {
                corrida[posVetor] += 1;
                i += 1;
            }
        }
        i = 0;
        double somaFo = 0;
        double somaGx = 0;
        double somaFx = 0;
        double maiorGx_Fx = 0;
        while (i < 6) {
            somaFo += corrida[i];
            i++;
        }
        i = 0;
        while (i < 6) {
            somaGx += (corrida[i] / somaFo);
            somaFx += ((i + 1) / fator[i]);
            if (maiorGx_Fx < Math.abs(somaGx - somaFx)) {
                maiorGx_Fx = Math.abs(somaGx - somaFx);
            }
            double aux = (corrida[i] / somaFo);
            double aux2 = ((i + 1) / fator[i]);
            modelo.addRow(new Object[]{
                (i + 1),
                corrida[i],
                df2.format(aux),
                df2.format(somaGx),
                df2.format(aux2),
                df2.format(somaFx),
                df2.format(Math.abs(somaGx - somaFx))
            }
            );
            i++;
        }
        kSCalcCorridaDes.setText("K_S: " + df2.format(maiorGx_Fx));
        kSCorridaDes.setText("K_S 5%: " + df2.format((1.36 / Math.sqrt(somaFo))));
        if (maiorGx_Fx < (1.36 / Math.sqrt(somaFo))) {
            aceitaCorridaDes.setText("Aceita H0, corridas não diferem das esperadas");
        } else {
            aceitaCorridaDes.setText("Rejeita H0, corridas diferem das esperadas");
        }
    }

    public void intervalo(double digito) {
        DefaultTableModel modelo = (DefaultTableModel) tabelaIntervalo.getModel();
        modelo.setNumRows(0);
        DecimalFormat df = new DecimalFormat("0.0");
        DecimalFormat df2 = new DecimalFormat("0.000");
        double vetor[] = new double[TOTALN];
        int intervalo = 0;
        int maiorIntervalo = 0;
        int i = 0;
        while (i < TOTALN) {
            if (lista.get(i) >= digito && lista.get(i) < (digito + 0.1)) {
                vetor[intervalo] += 1;
                if (maiorIntervalo < intervalo) {
                    maiorIntervalo = intervalo;
                }
                intervalo = 0;
            } else {
                intervalo += 1;
            }
            i++;
        }
        vetor[intervalo] += 1;
        i = 0;
        double totalIntervalo = 0;
        double acumuladoIntervalo = 0;
        double somaFx = 0;
        double maiorGx_Fx = 0;
        while (i < maiorIntervalo) {
            totalIntervalo += vetor[i];
            i++;
        }
        i = 0;
        while (i <= maiorIntervalo) {
            acumuladoIntervalo += vetor[i] / totalIntervalo;
            somaFx += Math.pow(0.9, i) * 0.1;
            if (Math.abs(acumuladoIntervalo - somaFx) > maiorGx_Fx) {
                maiorGx_Fx = Math.abs(acumuladoIntervalo - somaFx);
            }
            modelo.addRow(new Object[]{
                i,
                vetor[i],
                df2.format(vetor[i] / totalIntervalo),
                df2.format(acumuladoIntervalo),
                df2.format(Math.pow(0.9, i) * 0.1),
                df2.format(somaFx),
                df2.format(Math.abs(acumuladoIntervalo - somaFx))
            }
            );
            i++;
        }
        kSCalcIntervalo.setText("K_S: " + df2.format(maiorGx_Fx));
        kSIntervalo.setText("K_S 5%: " + df2.format(1.36 / Math.sqrt(totalIntervalo)));
        if (maiorGx_Fx < (1.36 / Math.sqrt(totalIntervalo))) {
            aceitaIntervalo.setText("Aceita H0, os intervalos não diferem dos esperados");
        } else {
            aceitaIntervalo.setText("Rejeita H0, os intervalos diferem dos esperados");
        }
    }

    public void permutacoes() {
        DefaultTableModel modelo = (DefaultTableModel) tabelaPermutacoes.getModel();
        modelo.setNumRows(0);
        DecimalFormat df = new DecimalFormat("0.0");
        DecimalFormat df2 = new DecimalFormat("0.000");
        double permuta[] = new double[6];
        int i = 0;
        while (i < TOTALN) {
            if (lista.get(i) < lista.get(i + 1) && lista.get(i + 1) < lista.get(i + 2)) {
                permuta[0] += 1;
            } else if (lista.get(i) < lista.get(i + 2) && lista.get(i + 2) < lista.get(i + 1)) {
                permuta[1] += 1;
            } else if (lista.get(i + 1) < lista.get(i) && lista.get(i) < lista.get(i + 2)) {
                permuta[2] += 1;
            } else if (lista.get(i + 1) < lista.get(i + 2) && lista.get(i + 2) < lista.get(i)) {
                permuta[3] += 1;
            } else if (lista.get(i + 2) < lista.get(i) && lista.get(i) < lista.get(i + 1)) {
                permuta[4] += 1;
            } else if (lista.get(i + 2) < lista.get(i + 1) && lista.get(i + 1) < lista.get(i)) {
                permuta[5] += 1;
            }
            i += 3;
        }
        i = 0;
        double somaPermuta = 0;
        double aux;
        double aux2;
        double maxFxGx = 0;
        double somaFo = 0;
        while (i < 6) {
            aux = 0.1667;
            aux2 = 0.1667 * (i + 1);
            somaFo += permuta[i];
            somaPermuta += permuta[i] / (TOTALN / 3);
            if (maxFxGx < Math.abs(aux2 - somaPermuta)) {
                maxFxGx = Math.abs(aux2 - somaPermuta);
            }
            modelo.addRow(new Object[]{
                i + 1,
                permuta[i],
                df2.format(permuta[i] / (TOTALN / 3)),
                df2.format(somaPermuta),
                df2.format(aux),
                df2.format(aux2),
                df2.format(Math.abs(aux2 - somaPermuta))
            }
            );
            i++;
        }
        kSCalcPermuta.setText("K_S: " + df2.format(maxFxGx));
        kSPermuta.setText("K_S 5%: " + df2.format(1.36 / Math.sqrt(somaFo)));
        if (maxFxGx < (1.36 / Math.sqrt(somaFo))) {
            aceitaPermuta.setText("Aceita H0, os ordenados não diferem dos esperados");
        } else {
            aceitaPermuta.setText("Rejeita H0, os ordenados diferem dos esperados");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        calculando = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        sementeMATEDURAI = new javax.swing.JTextField();
        botaoMATEDURAI = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelaK_S = new javax.swing.JTable();
        kSCalcUni = new javax.swing.JLabel();
        kS5Uni = new javax.swing.JLabel();
        aceitaUni = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabelaCorridaAsc = new javax.swing.JTable();
        kSCalcCorridaAsc = new javax.swing.JLabel();
        kSCorridaAsc = new javax.swing.JLabel();
        aceitaCorridaAsc = new javax.swing.JLabel();
        tempoGasto = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabelaCorridaDes = new javax.swing.JTable();
        kSCalcCorridaDes = new javax.swing.JLabel();
        kSCorridaDes = new javax.swing.JLabel();
        aceitaCorridaDes = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        digitoIntervalo = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tabelaIntervalo = new javax.swing.JTable();
        kSCalcIntervalo = new javax.swing.JLabel();
        kSIntervalo = new javax.swing.JLabel();
        aceitaIntervalo = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tabelaPermutacoes = new javax.swing.JTable();
        kSCalcPermuta = new javax.swing.JLabel();
        kSPermuta = new javax.swing.JLabel();
        aceitaPermuta = new javax.swing.JLabel();
        comboBox = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setMaximumSize(new java.awt.Dimension(1200, 600));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(1200, 600));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(1200, 600));

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));
        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        calculando.setBackground(new java.awt.Color(255, 0, 0));
        calculando.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        calculando.setForeground(new java.awt.Color(255, 0, 0));
        calculando.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        calculando.setText("     ");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("Semente:");

        sementeMATEDURAI.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        sementeMATEDURAI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sementeMATEDURAIActionPerformed(evt);
            }
        });

        botaoMATEDURAI.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        botaoMATEDURAI.setText("Rodar");
        botaoMATEDURAI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoMATEDURAIActionPerformed(evt);
            }
        });

        tabelaK_S.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tabelaK_S.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Li", "Ls", "Fo", "Fe", "(Fo-Fe)^2/Fe", "p'i", "G(x)", "F(x)", "| F(X) - G(X) |"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tabelaK_S);
        if (tabelaK_S.getColumnModel().getColumnCount() > 0) {
            tabelaK_S.getColumnModel().getColumn(0).setResizable(false);
            tabelaK_S.getColumnModel().getColumn(1).setResizable(false);
            tabelaK_S.getColumnModel().getColumn(1).setHeaderValue("");
            tabelaK_S.getColumnModel().getColumn(2).setResizable(false);
            tabelaK_S.getColumnModel().getColumn(3).setResizable(false);
            tabelaK_S.getColumnModel().getColumn(4).setResizable(false);
            tabelaK_S.getColumnModel().getColumn(4).setHeaderValue("(Fo-Fe)^2/Fe");
            tabelaK_S.getColumnModel().getColumn(5).setResizable(false);
            tabelaK_S.getColumnModel().getColumn(6).setResizable(false);
            tabelaK_S.getColumnModel().getColumn(7).setResizable(false);
            tabelaK_S.getColumnModel().getColumn(8).setResizable(false);
        }

        kSCalcUni.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        kSCalcUni.setForeground(new java.awt.Color(255, 255, 255));
        kSCalcUni.setText("K_S");

        kS5Uni.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        kS5Uni.setForeground(new java.awt.Color(255, 255, 255));
        kS5Uni.setText("K_S 5%");

        aceitaUni.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        aceitaUni.setForeground(new java.awt.Color(51, 51, 255));
        aceitaUni.setText("Resultado");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Geradores");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("Teste das corridas ascendente");

        tabelaCorridaAsc.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tabelaCorridaAsc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Corrida", "Fo", "p'i", "G(x)", "f(x)", "F(x)", "| F(X) - G(X) |"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tabelaCorridaAsc);
        if (tabelaCorridaAsc.getColumnModel().getColumnCount() > 0) {
            tabelaCorridaAsc.getColumnModel().getColumn(0).setResizable(false);
            tabelaCorridaAsc.getColumnModel().getColumn(1).setResizable(false);
            tabelaCorridaAsc.getColumnModel().getColumn(2).setResizable(false);
            tabelaCorridaAsc.getColumnModel().getColumn(3).setResizable(false);
            tabelaCorridaAsc.getColumnModel().getColumn(4).setResizable(false);
            tabelaCorridaAsc.getColumnModel().getColumn(5).setResizable(false);
            tabelaCorridaAsc.getColumnModel().getColumn(6).setResizable(false);
        }

        kSCalcCorridaAsc.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        kSCalcCorridaAsc.setForeground(new java.awt.Color(255, 255, 255));
        kSCalcCorridaAsc.setText("K_S");

        kSCorridaAsc.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        kSCorridaAsc.setForeground(new java.awt.Color(255, 255, 255));
        kSCorridaAsc.setText("K_S 5%");

        aceitaCorridaAsc.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        aceitaCorridaAsc.setForeground(new java.awt.Color(51, 51, 255));
        aceitaCorridaAsc.setText("Resultado");

        tempoGasto.setFont(new java.awt.Font("Tahoma", 2, 20)); // NOI18N
        tempoGasto.setForeground(new java.awt.Color(204, 204, 204));
        tempoGasto.setText("Tempo");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Teste das corridas descendente");

        tabelaCorridaDes.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tabelaCorridaDes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Corrida", "Fo", "p'i", "G(x)", "f(x)", "F(x)", "| F(X) - G(X) |"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tabelaCorridaDes);
        if (tabelaCorridaDes.getColumnModel().getColumnCount() > 0) {
            tabelaCorridaDes.getColumnModel().getColumn(0).setResizable(false);
            tabelaCorridaDes.getColumnModel().getColumn(1).setResizable(false);
            tabelaCorridaDes.getColumnModel().getColumn(2).setResizable(false);
            tabelaCorridaDes.getColumnModel().getColumn(3).setResizable(false);
            tabelaCorridaDes.getColumnModel().getColumn(4).setResizable(false);
            tabelaCorridaDes.getColumnModel().getColumn(5).setResizable(false);
            tabelaCorridaDes.getColumnModel().getColumn(6).setResizable(false);
        }

        kSCalcCorridaDes.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        kSCalcCorridaDes.setForeground(new java.awt.Color(255, 255, 255));
        kSCalcCorridaDes.setText("K_S");

        kSCorridaDes.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        kSCorridaDes.setForeground(new java.awt.Color(255, 255, 255));
        kSCorridaDes.setText("K_S 5%");

        aceitaCorridaDes.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        aceitaCorridaDes.setForeground(new java.awt.Color(51, 51, 255));
        aceitaCorridaDes.setText("Resultado");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText("Dígito intervalo:");

        digitoIntervalo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        digitoIntervalo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                digitoIntervaloActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText("Teste intervalo");

        tabelaIntervalo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tabelaIntervalo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Intervalo", "Fo", "p'i", "G(x)", "f(x)", "F(x)", "| F(X) - G(X) |"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(tabelaIntervalo);
        if (tabelaIntervalo.getColumnModel().getColumnCount() > 0) {
            tabelaIntervalo.getColumnModel().getColumn(0).setResizable(false);
            tabelaIntervalo.getColumnModel().getColumn(1).setResizable(false);
            tabelaIntervalo.getColumnModel().getColumn(2).setResizable(false);
            tabelaIntervalo.getColumnModel().getColumn(3).setResizable(false);
            tabelaIntervalo.getColumnModel().getColumn(4).setResizable(false);
            tabelaIntervalo.getColumnModel().getColumn(5).setResizable(false);
            tabelaIntervalo.getColumnModel().getColumn(6).setResizable(false);
        }

        kSCalcIntervalo.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        kSCalcIntervalo.setForeground(new java.awt.Color(255, 255, 255));
        kSCalcIntervalo.setText("K_S");

        kSIntervalo.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        kSIntervalo.setForeground(new java.awt.Color(255, 255, 255));
        kSIntervalo.setText("K_S 5%");

        aceitaIntervalo.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        aceitaIntervalo.setForeground(new java.awt.Color(51, 51, 255));
        aceitaIntervalo.setText("Resultado");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("Teste permutações");

        tabelaPermutacoes.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tabelaPermutacoes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Oi", "Fo", "p'i", "G(x)", "f(x)", "F(x)", "| F(X) - G(X) |"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(tabelaPermutacoes);
        if (tabelaPermutacoes.getColumnModel().getColumnCount() > 0) {
            tabelaPermutacoes.getColumnModel().getColumn(0).setResizable(false);
            tabelaPermutacoes.getColumnModel().getColumn(1).setResizable(false);
            tabelaPermutacoes.getColumnModel().getColumn(2).setResizable(false);
            tabelaPermutacoes.getColumnModel().getColumn(3).setResizable(false);
            tabelaPermutacoes.getColumnModel().getColumn(4).setResizable(false);
            tabelaPermutacoes.getColumnModel().getColumn(5).setResizable(false);
            tabelaPermutacoes.getColumnModel().getColumn(6).setResizable(false);
        }

        kSCalcPermuta.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        kSCalcPermuta.setForeground(new java.awt.Color(255, 255, 255));
        kSCalcPermuta.setText("K_S");

        kSPermuta.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        kSPermuta.setForeground(new java.awt.Color(255, 255, 255));
        kSPermuta.setText("K_S 5%");

        aceitaPermuta.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        aceitaPermuta.setForeground(new java.awt.Color(51, 51, 255));
        aceitaPermuta.setText("Resultado");

        comboBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        comboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Gerador MATEDURAI", "Gerador 1", "Gerador DEC", "Gerador SAS" }));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("Teste Uniformidade");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1271, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1271, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 1271, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 1271, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 1271, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(kSCalcUni, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(kS5Uni, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(aceitaUni, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(kSCalcCorridaAsc, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(kSCorridaAsc, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(aceitaCorridaAsc, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(kSCalcCorridaDes, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(kSCorridaDes, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(aceitaCorridaDes, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(kSCalcIntervalo, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(kSIntervalo, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(aceitaIntervalo, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(kSCalcPermuta, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(kSPermuta, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(aceitaPermuta, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(calculando, javax.swing.GroupLayout.PREFERRED_SIZE, 1276, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 1276, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 1276, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 1276, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 1276, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(sementeMATEDURAI, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(digitoIntervalo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(comboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(botaoMATEDURAI, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(tempoGasto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 1286, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 1276, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 53, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(sementeMATEDURAI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel6))
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(digitoIntervalo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(botaoMATEDURAI)
                                .addComponent(comboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(6, 6, 6))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tempoGasto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(calculando, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addGap(12, 12, 12)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(kSCalcUni)
                .addGap(18, 18, 18)
                .addComponent(kS5Uni)
                .addGap(18, 18, 18)
                .addComponent(aceitaUni)
                .addGap(19, 19, 19)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(kSCalcCorridaAsc)
                .addGap(18, 18, 18)
                .addComponent(kSCorridaAsc)
                .addGap(18, 18, 18)
                .addComponent(aceitaCorridaAsc)
                .addGap(19, 19, 19)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(kSCalcCorridaDes)
                .addGap(18, 18, 18)
                .addComponent(kSCorridaDes)
                .addGap(18, 18, 18)
                .addComponent(aceitaCorridaDes)
                .addGap(19, 19, 19)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(kSCalcIntervalo)
                .addGap(18, 18, 18)
                .addComponent(kSIntervalo)
                .addGap(18, 18, 18)
                .addComponent(aceitaIntervalo)
                .addGap(19, 19, 19)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(kSCalcPermuta)
                .addGap(18, 18, 18)
                .addComponent(kSPermuta)
                .addGap(18, 18, 18)
                .addComponent(aceitaPermuta)
                .addContainerGap(536, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1320, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 920, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sementeMATEDURAIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sementeMATEDURAIActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sementeMATEDURAIActionPerformed

    private void botaoMATEDURAIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoMATEDURAIActionPerformed
        if (sementeMATEDURAI.getText() != null && Integer.parseInt(sementeMATEDURAI.getText()) >= 0 && digitoIntervalo.getText() != null && Integer.parseInt(digitoIntervalo.getText()) >= 0) {
            calculando.setText("CALCULANDO...");
            fatorial();
            if (comboBox.getSelectedIndex() == 0) {
                geraArquivo(Integer.parseInt(sementeMATEDURAI.getText()), (Math.pow(2, 61)) - 1, 8 * 3 + 3, 167, System.getProperty("user.dir") + "/CRIALEOMATEDURAI.txt");
            } else if (comboBox.getSelectedIndex() == 1) {
                geraArquivo(Integer.parseInt(sementeMATEDURAI.getText()), (Math.pow(2, 31)) - 1, 16807, 0, System.getProperty("user.dir") + "/CRIALEOGERADOR1.txt");
            } else if (comboBox.getSelectedIndex() == 2) {
                geraArquivo(Integer.parseInt(sementeMATEDURAI.getText()), Math.pow(2, 32), 69069, 1, System.getProperty("user.dir") + "/CRIALEOGERADORDEC.txt");
            } else if (comboBox.getSelectedIndex() == 3) {
                geraArquivo(Integer.parseInt(sementeMATEDURAI.getText()), (Math.pow(2, 31)) - 1, 397204094, 0, System.getProperty("user.dir") + "/CRIALEOGERADORSAS.txt");
            }
            k_S_Gera();
            corridaAsce();
            corridaDesc();
            intervalo(Double.parseDouble("0." + digitoIntervalo.getText()));
            permutacoes();
            calculando.setText("");
        }
    }//GEN-LAST:event_botaoMATEDURAIActionPerformed

    private void digitoIntervaloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_digitoIntervaloActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_digitoIntervaloActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Tela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Tela().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel aceitaCorridaAsc;
    private javax.swing.JLabel aceitaCorridaDes;
    private javax.swing.JLabel aceitaIntervalo;
    private javax.swing.JLabel aceitaPermuta;
    private javax.swing.JLabel aceitaUni;
    private javax.swing.JButton botaoMATEDURAI;
    private javax.swing.JLabel calculando;
    private javax.swing.JComboBox<String> comboBox;
    private javax.swing.JTextField digitoIntervalo;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JLabel kS5Uni;
    private javax.swing.JLabel kSCalcCorridaAsc;
    private javax.swing.JLabel kSCalcCorridaDes;
    private javax.swing.JLabel kSCalcIntervalo;
    private javax.swing.JLabel kSCalcPermuta;
    private javax.swing.JLabel kSCalcUni;
    private javax.swing.JLabel kSCorridaAsc;
    private javax.swing.JLabel kSCorridaDes;
    private javax.swing.JLabel kSIntervalo;
    private javax.swing.JLabel kSPermuta;
    private javax.swing.JTextField sementeMATEDURAI;
    private javax.swing.JTable tabelaCorridaAsc;
    private javax.swing.JTable tabelaCorridaDes;
    private javax.swing.JTable tabelaIntervalo;
    private javax.swing.JTable tabelaK_S;
    private javax.swing.JTable tabelaPermutacoes;
    private javax.swing.JLabel tempoGasto;
    // End of variables declaration//GEN-END:variables
}
