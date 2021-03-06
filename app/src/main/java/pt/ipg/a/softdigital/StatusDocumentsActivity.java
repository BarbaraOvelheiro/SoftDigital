package pt.ipg.a.softdigital;

import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toolbar;

public class StatusDocumentsActivity extends AppCompatActivity {
    /**
     * Método que executa o programa
     * public = É visto em qualquer lugar da aplicação. É o modificador de acesso
     * static = é iniciado automaticamente pela JVM, sem precisar de uma instância
     * void = Método sem retorno (retorno vazio)
     * main = Nome do método, que é obrigatório ser este. Recebe como parâmetro um array de String.
     * String[] args = Array de argumentos que podem ser repassados na chamada do programa.
     **/

    TabLayout tabLayout;
    TabItem AllDocuments_TabItem;
    TabItem NecessarySign_TabItem;
    TabItem WaitingSignatures_TabItem;
    TabItem Done_TabItem;
    ViewPager viewPager;
    PageAdapter pageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_documents);

        getSupportActionBar().setTitle(R.string.files);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout = findViewById(R.id.tabs);

        AllDocuments_TabItem = findViewById(R.id.AllDocuments_TabItem);
        NecessarySign_TabItem = findViewById(R.id.NecessarySign_TabItem);
        WaitingSignatures_TabItem = findViewById(R.id.WaitingSignatures_TabItem);
        Done_TabItem = findViewById(R.id.Done_TabItem);

        viewPager = findViewById(R.id.viewPager);

        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}
