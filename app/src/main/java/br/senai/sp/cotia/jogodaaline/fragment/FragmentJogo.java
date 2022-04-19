package br.senai.sp.cotia.jogodaaline.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.location.GnssAntennaInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Random;

import br.senai.sp.cotia.jogodaaline.R;
import br.senai.sp.cotia.jogodaaline.databinding.FragmentJogoBinding;
import br.senai.sp.cotia.jogodaaline.util.PrefsUtil;

public class FragmentJogo extends Fragment {
    //variável para acessar os elementos view
    private FragmentJogoBinding binding;
    //vetor para agrupar os botões
    private Button [] botoes;

    //variavel que representa o tabeluleiro
    private String [][] tabuleiro;

    //variável para os símbolos que vai trocar no jogo
    private String simbJog1, simbJog2, simbolo;

    private AlertDialog alertaa;
    //sortear que começa o jogo, variável random para sortear quem começa
    private Random random;
    //variável para contrar o número de jogadas
    private int numJogadas = 0;

    //variáveis para placar
    private int placarJog1 = 0, placarJog2 = 0, velha = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // instancia o binding
        binding = FragmentJogoBinding.inflate(inflater, container, false);
        // cria o alert
        AlertDialog.Builder alert = new AlertDialog.Builder(this.getActivity());
        alert.setMessage("Deseja resetar o jogo?");
        alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(), "Resetado com sucesso?", Toast.LENGTH_SHORT).show();

                placarJog1 = 0;
                placarJog2 = 0;
                velha = 0;
                resetaJogo();
                alertaa.show();
                atualizarPlacar();

            }
        });

        alert.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(), "Continue jogando", Toast.LENGTH_SHORT).show();
            }
        });
        alertaa = alert.create();
        // habilita o menu neste fragment
        setHasOptionsMenu(true);


        //instancia o vetor
        botoes  = new Button[9];
        // agrupar os botões no vetor
        botoes[0] = binding.bt00;
        botoes[1] = binding.bt01;
        botoes[2] = binding.bt02;
        botoes[3] = binding.bt10;
        botoes[4] = binding.bt11;
        botoes[5] = binding.bt12;
        botoes[6] = binding.bt20;
        botoes[7] = binding.bt21;
        botoes[8] = binding.bt22;

        // Associa o Listener aos botões
        for (Button bt : botoes){
            bt.setOnClickListener(listenerBotoes);
        }

        // instancia o tabuleiro
        tabuleiro = new String[3][3];

        // preencher o tabuleiro com ""
        for (String[] vetor : tabuleiro){
            Arrays.fill(vetor, "");
        }

        /* FAz a msm coisa que o de cima
        for(int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                tabuleiro[i][j] = "";
            }
        }*/

        //instancia o Random
        random = new Random();

        //define os símbolos dos jogadores
        simbJog1 = PrefsUtil.getSimboloJog1(getContext());
        simbJog2 = PrefsUtil.getSimboloJog2(getContext());
//altera o simbolo do jogador no placar
        binding.placarJogador1.setText(getResources().getString(R.string.jogadorum, simbJog1));
        binding.placarJogador2.setText(getResources().getString(R.string.jogadordois, simbJog2));

        //sorteia quem inicia o jogo
        sorteia();
        //atualiza a vez
        atualizaVez();

        //retorna a view do Fragment
        return  binding.getRoot();
    }

    private void sorteia(){
        // caso contrario o Jogador 2 começa
        // caso Random gere um valor verdadeiro, jogador 1 começa
        if(random.nextBoolean()){
            simbolo = simbJog1;
        }else{
            simbolo = simbJog2;
        }
    }

    private void atualizaVez(){
        if(simbolo.equals(simbJog1)){
            binding.placarJogador1.setBackgroundColor(getResources().getColor(R.color.black));
            binding.placarJogador2.setBackgroundColor(getResources().getColor(R.color.white));
        }else{
            binding.placarJogador2.setBackgroundColor(getResources().getColor(R.color.rosa));
            binding.placarJogador1.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }
    private void resetaJogo(){
        // zerarando o tabuleiro
        for (String[] vetor : tabuleiro){
            Arrays.fill(vetor, "");
        }
        //vai resetar os botoes
        for (Button botao : botoes){
            botao.setBackgroundColor(getResources().getColor(R.color.azul));
            botao.setText("");
            botao.setClickable(true);
        }
        sorteia();
        atualizaVez();
        numJogadas = 0;

    }

    private boolean vencedor(){
        //verifica se venceu nas linhas
        for (int i = 0; i < 3; i++){
            if (tabuleiro[i][0].equals(simbolo) && tabuleiro[i][1].equals(simbolo) && tabuleiro[i][2].equals(simbolo)){
                return true;
            }
        }

        // verificar se venceu na coluna
        for (int i = 0; i < 3; i++){
            if (tabuleiro[0][i].equals(simbolo) && tabuleiro[1][i].equals(simbolo) && tabuleiro[2][i].equals(simbolo)){
                return true;
            }
        }
        // verifica se venceu nas diagonais
        if (tabuleiro[0][0].equals(simbolo) && tabuleiro[1][1].equals(simbolo) && tabuleiro[2][2].equals(simbolo)){
            return true;
        }
        if (tabuleiro[0][2].equals(simbolo) && tabuleiro[1][1].equals(simbolo) && tabuleiro[2][0].equals(simbolo)) {
            return true;
        }
        return false;
    }

    private void atualizarPlacar(){
        binding.placarJogador1.setText(placarJog1+"");
        binding.placarJogador2.setText(placarJog2+"");
        binding.placarVelha.setText(velha+"");
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater){
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // verifica qual botão foi clicado no menu
        switch (item.getItemId()){
            case R.id.menu_resetar:
                alertaa.show();

                break;
            // caso tenha clicado no preferencias
            case R.id.menu_prefs:
                NavHostFragment.findNavController(FragmentJogo.this).navigate(R.id.action_fragmentJogo_to_prefFragment);
                break;
        }
        return true;
    }

    private  View.OnClickListener listenerBotoes = btPress -> {
        //incrementas as jogadas
        numJogadas ++;
        // pega o nome do botão
        String nomeBotao = getContext().getResources().getResourceName(btPress.getId());
        // extrai os 2 últimos caracteres do nomeBotão
        String posicao = nomeBotao.substring(nomeBotao.length()-2);
        //extrai a posicao em linha e coluna
        int linha = Character.getNumericValue(posicao.charAt(0));
        int coluna = Character.getNumericValue(posicao.charAt(1));
        //Marca no tabuleiro o simbolo que foi jogado
        tabuleiro[linha][coluna] = simbolo;
        // faz um casting de View para Button
        Button botao = (Button) btPress;
        //trocar o texto do botão que foi clicado
        botao.setText(simbolo);
        //desabilitar o botão
        botao.setClickable(false);
        //trocar o backgroud o botão
        botao.setBackgroundColor(Color.BLUE);
        //verifica se venceu
        if(numJogadas >= 5 && vencedor()){
            // exibe um Toast informando que o jogador venceu
            Toast.makeText(getContext(),R.string.venceu, Toast.LENGTH_LONG).show();
            // verifica quem venceu e atualiza o placar
            if (simbolo.equals(simbJog1)){
                placarJog1++;
            }else if (simbolo.equals(simbJog2)){
                placarJog2++;
            }else {
                velha++;
            }
            //atualiza o placar
            atualizarPlacar();
            //resetando o tabuleiro
            resetaJogo();

            //exibe que deu velha
        }else if(numJogadas == 9){
            Toast.makeText(getContext(),R.string.perdeu, Toast.LENGTH_LONG).show();
            // resetando tabuleiro
            resetaJogo();
            velha++;
            atualizarPlacar();
        }else{
            //inverter a vez usando operador ternário
            simbolo = simbolo.equals(simbJog1) ? simbJog2 : simbJog1;
            //atualiza vez
            atualizaVez();
        }
    };
    public void onStart(){
        super.onStart();

        // para "sumir" com a toolbar
        // pegar uma referência do tipo AppCompatActivity
        AppCompatActivity minhaActivity = (AppCompatActivity) getActivity();
        // ocultar a actionBar
        minhaActivity.getSupportActionBar().show();
        minhaActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }
}