package br.senai.sp.cotia.jogodaaline.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.senai.sp.cotia.jogodaaline.R;
import br.senai.sp.cotia.jogodaaline.databinding.FragmentInicioBinding;


public class FragmentInicio extends Fragment {


    public static FragmentInicioBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //instancia o binding
        binding = FragmentInicioBinding.inflate(inflater, container, false);
        //ação do botão que leva ao fragment do jogo
        binding.irParaJogo.setOnClickListener(v ->{
            NavHostFragment.findNavController(FragmentInicio.this).navigate(R.id.action_fragmentInicio_to_fragmentJogo);

        });

        return binding.getRoot();

    }
    @Override
    public void onStart(){
        super.onStart();
        // para sumir com a toolbar da tela do início
        //pegar uma referência do tipo AppCompaActivity
        AppCompatActivity minhaActivity = (AppCompatActivity) getActivity();
        minhaActivity.getSupportActionBar().hide();

        //colocar no fragment do jogo, trocando hide por show
    }
}