package com.example.samsungblockchainsdk;

import androidx.appcompat.app.AppCompatActivity;

import com.samsung.android.sdk.blockchain.account.Account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;
import com.samsung.android.sdk.blockchain.CoinType;
import com.samsung.android.sdk.blockchain.ListenableFutureTask;
import com.samsung.android.sdk.blockchain.SBlockchain;
import com.samsung.android.sdk.blockchain.account.ethereum.EthereumAccount;
import com.samsung.android.sdk.blockchain.coinservice.CoinNetworkInfo;
import com.samsung.android.sdk.blockchain.coinservice.CoinServiceFactory;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumService;
import com.samsung.android.sdk.blockchain.exception.HardwareWalletException;
import com.samsung.android.sdk.blockchain.exception.RootSeedChangedException;
import com.samsung.android.sdk.blockchain.network.EthereumNetworkType;
import com.samsung.android.sdk.blockchain.ui.CucumberWebView;
import com.samsung.android.sdk.blockchain.ui.OnSendTransactionListener;
import com.samsung.android.sdk.blockchain.wallet.HardwareWallet;
import com.samsung.android.sdk.blockchain.wallet.HardwareWalletType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Main2Activity extends AppCompatActivity implements OnSendTransactionListener {
    Button connect;
    Button generateAccount;
    Button getAccounts;
    Button paymentsheet;
  //  Button sendSmartContract;
    // 웹뷰버튼
    Button webViewInitBtn;


    private SBlockchain sBlockchain ;
    private HardwareWallet wallet;
    private CucumberWebView webView; // 웹뷰 변수 선언
    Account generateAccount1;
    private List<Account> accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // Blockchain SDK Init
        sBlockchain = new SBlockchain(); // Blockchain SDK Init
        try {
            sBlockchain.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 끝


        //이벤트 리스너 호출 //
        connect = findViewById(R.id.connect);
        generateAccount =  findViewById(R.id.generateAccount);
        getAccounts = findViewById(R.id.getAccounts);
        paymentsheet = findViewById(R.id.paymentsheet);
        //cucumber 이벤트 리스너 호출
        webViewInitBtn = findViewById(R.id.webviewinit); // 웹뷰버튼 객체생성
        webView = findViewById(R.id.cucumber); //큐컴버 웹뷰 객체생성

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"connect버튼이 눌러졌습니다.",Toast.LENGTH_SHORT).show();
                connected();
            }
        });

        generateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"generateAccount버튼이 눌러졌습니다.",Toast.LENGTH_SHORT).show();
                generate();
            }
        });

        getAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"getAccounts버튼이 눌러졌습니다.",Toast.LENGTH_SHORT).show();
                setgetAccounts();

            }
        });

      paymentsheet.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Toast.makeText(getApplicationContext(),"paymentsheet버튼이 눌러졌습니다.",Toast.LENGTH_SHORT).show();
              setPaymentsheet();
          }
      });

      webViewInitBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              webViewInit();
          }
      });

        //이벤트 리스너 호출  끝//
    }




    //1. connect
    public void connected(){
       sBlockchain.getHardwareWalletManager()
               .connect(HardwareWalletType.SAMSUNG,true) //하드웨어 wallet을 반환하는 형태입니다.
               .setCallback(new ListenableFutureTask.Callback<HardwareWallet>() { //비동기화  자기혼자 쓰레드로 돌아가기 위해서
                   @Override
                   public void onSuccess(HardwareWallet hardwareWallet) {
                        wallet = hardwareWallet;
                   }

                   @Override
                   public void onFailure(ExecutionException e) {
                       Throwable cause = e.getCause();

                       if (cause instanceof HardwareWalletException) {
                           // handling hardware wallet error
                       } else if (cause instanceof RootSeedChangedException) {
                           // handling root seed changed exception
                       }
                   }

                   @Override
                   public void onCancelled(InterruptedException e) {

                   }
               });
    }




    //2. generateAccount
    private  void generate() { //계정생성
        CoinNetworkInfo coinNetworkInfo = new CoinNetworkInfo(
                CoinType.ETH,
                EthereumNetworkType.ROPSTEN,
                "https://ropsten.infura.io/v3/70ddb1f89ca9421885b6268e847a459d"// 퍼블릭 노드인 공짜인 주소를 가져온거야  + ropsten.infura.io로 들어가서 퍼블릭 노드 불러오기
        );

        sBlockchain.getAccountManager()
                .generateNewAccount(wallet, coinNetworkInfo)
                .setCallback(new ListenableFutureTask.Callback< Account >() {
                    @Override
                    public void onSuccess(Account account) {
                        generateAccount1 = account;
                        Log.e("MyApp", account.toString());
                    }

                    @Override
                    public void onFailure(@NotNull ExecutionException e) {
                        Log.e("MyApp fail",e.toString());

                    }

                    @Override
                    public void onCancelled(@NotNull InterruptedException e) {
                        Log.e("MyApp cancel",e.toString());
                    }
                });

    }



    //3. getAccounts
    private  void setgetAccounts() { //
        accounts = sBlockchain.getAccountManager()
                .getAccounts(wallet.getWalletId(),CoinType.ETH, EthereumNetworkType.ROPSTEN);
        Log.d("MyApp", Arrays.toString(new List[]{accounts})); // 리스트형태로 로그메시지 출력
    }



    //4. paymentsheet
    private  void setPaymentsheet(){ //계정으로 0.01이더만큼 전송합니다.
        CoinNetworkInfo coinNetworkInfo = new CoinNetworkInfo(
                CoinType.ETH,
                EthereumNetworkType.ROPSTEN,
                "https://ropsten.infura.io/v3/70ddb1f89ca9421885b6268e847a459d"// 퍼블릭 노드인 공짜인 주소를 가져온거야  + ropsten.infura.io로 들어가서 퍼블릭 노드 불러오기
        );

        sBlockchain.getAccountManager() //우리 어카운트가 잇겟죠
                .getAccounts(wallet.getWalletId(),
                        CoinType.ETH,
                        EthereumNetworkType.ROPSTEN
                        );
        // 메타마스크와 삼성키스토어의 계좌번호는 서로 다르다. 규칙이 다르기 때문이다.

        EthereumService service = (EthereumService) CoinServiceFactory.getCoinService(this,coinNetworkInfo);
        Intent intent = service
                .createEthereumPaymentSheetActivityIntent(
                        this,
                        wallet,
                        (EthereumAccount) accounts.get(0),
                        "0x94e44C14e7A5863fed31a68AAD9bbc7d30d6A019",
                        new BigInteger("10000000000000000"),
                        null,
                        null
                );
        startActivityForResult(intent,0);


    }
    //5. 웹을 가져오는 실습
    private void webViewInit() {
        CoinNetworkInfo coinNetworkInfo = new CoinNetworkInfo(
                CoinType.ETH,
                EthereumNetworkType.ROPSTEN,
                "https://ropsten.infura.io/v3/70ddb1f89ca9421885b6268e847a459d"
        );
        EthereumService service = (EthereumService) CoinServiceFactory.getCoinService(this, coinNetworkInfo);

        accounts =  sBlockchain.getAccountManager()
                .getAccounts(wallet.getWalletId(), CoinType.ETH, EthereumNetworkType.ROPSTEN);

        webView.init(service, accounts.get(0), this);
        webView.loadUrl("https://faucet.metamask.io/");
    }

    @Override
    public void onSendTransaction(
            @NotNull String requestId,
            @NotNull EthereumAccount fromAccount,
            @NotNull String toAddress,
            @org.jetbrains.annotations.Nullable BigInteger value,
            @org.jetbrains.annotations.Nullable String data,
            @org.jetbrains.annotations.Nullable BigInteger nonce
    ) {
        HardwareWallet wallet =
                sBlockchain.getHardwareWalletManager().getConnectedHardwareWallet();

        Intent intent =
                webView.createEthereumPaymentSheetActivityIntent(
                        this,
                        requestId,
                        wallet,
                        toAddress,
                        value,
                        data,
                        nonce
                );

        startActivityForResult(intent, 0);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != 0) {
            return;
        }
        webView.onActivityResult(requestCode, resultCode, data);
    }



}