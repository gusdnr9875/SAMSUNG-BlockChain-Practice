package com.example.samsungblockchainsdk;

import androidx.appcompat.app.AppCompatActivity;

import com.samsung.android.sdk.blockchain.account.Account;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.samsung.android.sdk.blockchain.CoinType;
import com.samsung.android.sdk.blockchain.ListenableFutureTask;
import com.samsung.android.sdk.blockchain.SBlockchain;
import com.samsung.android.sdk.blockchain.coinservice.CoinNetworkInfo;
import com.samsung.android.sdk.blockchain.exception.AccountException;
import com.samsung.android.sdk.blockchain.exception.HardwareWalletException;
import com.samsung.android.sdk.blockchain.exception.RemoteClientException;
import com.samsung.android.sdk.blockchain.exception.RootSeedChangedException;
import com.samsung.android.sdk.blockchain.exception.SsdkUnsupportedException;
import com.samsung.android.sdk.blockchain.network.EthereumNetworkType;
import com.samsung.android.sdk.blockchain.wallet.HardwareWallet;
import com.samsung.android.sdk.blockchain.wallet.HardwareWalletType;

import org.jetbrains.annotations.NotNull;
import org.web3j.protocol.core.Ethereum;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Main2Activity extends AppCompatActivity {
    Button connect;
    Button sendSmartContract;
    Button paymentsheet;
    Button generateAccount;
    Button getAccounts;

    private SBlockchain sBlockchain ;
    private HardwareWallet wallet;
    Account generateAccount1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 추가 내용
        sBlockchain = new SBlockchain(); // Blockchain SDK Init
        try {
            sBlockchain.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 끝

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        //이벤트 리스너 호출 //
        connect = findViewById(R.id.connect);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"connect버튼이 눌러졌습니다.",Toast.LENGTH_SHORT).show();
                connected();
            }
        });

        generateAccount =  findViewById(R.id.generateAccount);
        generateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"generateAccount버튼이 눌러졌습니다.",Toast.LENGTH_SHORT).show();
                generate();
            }
        });


        getAccounts = findViewById(R.id.getAccounts);
        getAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"getAccounts버튼이 눌러졌습니다.",Toast.LENGTH_SHORT).show();
                setgetAccounts();

            }
        });


      paymentsheet = findViewById(R.id.paymentsheet);
      paymentsheet.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Toast.makeText(getApplicationContext(),"paymentsheet버튼이 눌러졌습니다.",Toast.LENGTH_SHORT).show();
          }
      });


      sendSmartContract = findViewById(R.id.sendSmartContract);
      sendSmartContract.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Toast.makeText(getApplicationContext()," sendSmartContract버튼이 눌러졌습니다.",Toast.LENGTH_SHORT).show();
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
        List<Account> accounts = sBlockchain.getAccountManager()
                .getAccounts(wallet.getWalletId(),CoinType.ETH, EthereumNetworkType.ROPSTEN);
        Log.d("MyApp", Arrays.toString(new List[]{accounts})); // 리스트형태로 로그메시지 출력
    }





}
