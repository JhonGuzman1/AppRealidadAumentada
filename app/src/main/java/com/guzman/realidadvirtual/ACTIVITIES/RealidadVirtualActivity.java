package com.guzman.realidadvirtual.ACTIVITIES;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.MotionEvent;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.ar.core.Anchor;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.Config;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Session;
import com.google.ar.core.exceptions.UnavailableException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.guzman.realidadvirtual.R;
import com.guzman.realidadvirtual.UTILS.PARAMETROSGLOBALES;
import com.guzman.realidadvirtual.UTILS.Permisos;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.os.Environment.getExternalStorageDirectory;

public class RealidadVirtualActivity extends AppCompatActivity {

    private ArFragment arFragment;
    private ModelRenderable modelRenderable;
    private static final double MIN_OPENGL_VERSION = 3.0;
    private Button btnTomarFoto,btnDialogAceptar;
    private Permisos permisos;
    private SweetAlertDialog alertDialog;
    private Dialog dialog;
    private static int MostrarMensajePantalla = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realidad_virtual);
        Iniciar();
    }


    private void Iniciar(){



        permisos = new Permisos(RealidadVirtualActivity.this);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fr_realidad_virtual);
        btnTomarFoto = findViewById(R.id.btn_tomar_foto);


        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(permisos.VerficarPermisos()){
                    arFragment.getArSceneView().getPlaneRenderer().setVisible(false);
                   /* for (TransformableNode vNode : ){
                        if (vNode.isSelected()){
                            vNode.getTransformationSystem().selectNode(null);
                        }
                    }*/

                   //Cargando
                    alertDialog = new SweetAlertDialog(RealidadVirtualActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                    alertDialog.getProgressHelper().setBarColor(R.color.colorVerde);
                    alertDialog.setTitle("Tomando Foto");
                    alertDialog.setCancelable(false);
                    alertDialog.show();

                    //Mostrar mensaje
                    dialog = new Dialog(RealidadVirtualActivity.this,R.style.SlideTheme);
                    //deshabilitamos el título por defecto
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    //obligamos al usuario a pulsar los botones para cerrarlo
                    dialog.setCancelable(false);
                    //establecemos el contenido de nuestro dialog
                    dialog.setContentView(R.layout.dialog_mensaje);


                    btnDialogAceptar = dialog.findViewById(R.id.btn_aceptar_dialog_mensaje);

                    btnDialogAceptar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    v.postDelayed(new Runnable() {
                            public void run() {

                                takePhoto();
                            }
                        }, 1000);

                }else {
                    permisos.SolicitarPermisos();
                }




            }
        });

        if(checkIsSupportedDeviceOrFinish(RealidadVirtualActivity.this)){


            IniciarModelo();
            ConfigurarModelo();
        }else {
            Toast.makeText(RealidadVirtualActivity.this, "Ha ocurrido un error ", Toast.LENGTH_LONG).show();
        }

    }

    private void IniciarModelo(){

        switch (PARAMETROSGLOBALES.IdImagen){

            case 1 :
                ModelRenderable.builder()
                        .setSource(RealidadVirtualActivity.this,R.raw.personaje01)
                        .build()
                        .thenAccept(renderable -> modelRenderable = renderable)
                        .exceptionally(throwable -> {
                            Toast.makeText(RealidadVirtualActivity.this, "Ha ocurrido un error al mostrar el modelo", Toast.LENGTH_LONG).show();
                            return null;
                        });
                break;
            case 2:
                ModelRenderable.builder()
                        .setSource(RealidadVirtualActivity.this,R.raw.personaje02)
                        .build()
                        .thenAccept(renderable -> modelRenderable = renderable)
                        .exceptionally(throwable -> {
                            Toast.makeText(RealidadVirtualActivity.this, "Ha ocurrido un error al mostrar el modelo", Toast.LENGTH_LONG).show();
                            return null;
                        });
                break;
            case 3:
                ModelRenderable.builder()
                        .setSource(RealidadVirtualActivity.this,R.raw.personaje03)
                        .build()
                        .thenAccept(renderable -> modelRenderable = renderable)
                        .exceptionally(throwable -> {
                            Toast.makeText(RealidadVirtualActivity.this, "Ha ocurrido un error al mostrar el modelo", Toast.LENGTH_LONG).show();
                            return null;
                        });
                break;

        }


    }

    private void ConfigurarModelo(){
        arFragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
            @Override
            public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
                Anchor anchor = hitResult.createAnchor();
                AnchorNode anchorNode = new AnchorNode(anchor);
                anchorNode.setParent(arFragment.getArSceneView().getScene());
                CrearModelo(anchorNode);
            }
        });
    }

    private void CrearModelo(AnchorNode anchorNode){

        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());

        switch (PARAMETROSGLOBALES.IdImagen){

            case 1 :
                  //   TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
                    // node.setLocalScale(new Vector3(0.25f, 0.25f, 0.25f));
                    node.getScaleController().setMaxScale(0.90f);
                    node.getScaleController().setMinScale(0.07f);

                    node.setParent(anchorNode);
                    node.setRenderable(modelRenderable);
                    node.select();
                    arFragment.getArSceneView().getPlaneRenderer().setVisible(false);
                break;
            case 2:
                   // TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
                    // node.setLocalScale(new Vector3(0.25f, 0.25f, 0.25f));
                    node.getScaleController().setMaxScale(0.79f);
                    node.getScaleController().setMinScale(0.07f);

                    node.setParent(anchorNode);
                    node.setRenderable(modelRenderable);
                    node.select();
                break;
            case 3 :
                //   TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
                // node.setLocalScale(new Vector3(0.25f, 0.25f, 0.25f));
                node.getScaleController().setMaxScale(0.89f);
                node.getScaleController().setMinScale(0.07f);

                node.setParent(anchorNode);
                node.setRenderable(modelRenderable);
                node.select();
                arFragment.getArSceneView().getPlaneRenderer().setVisible(false);
                break;

        }



    }

    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {

            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {

            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }



    private void takePhoto() {


        MostrarMensajePantalla = 0;
        File nuevaCarpeta = new File(getExternalStorageDirectory()+"/DCIM", "RealidadVirtual");
        nuevaCarpeta.mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);
        final String filename = getExternalStorageDirectory().toString() + "/DCIM/RealidadVirtual/" + fecha + ".jpg";

        ArSceneView view = arFragment.getArSceneView();

        // Create a bitmap the size of the scene view.
        final Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);

        // Create a handler thread to offload the processing of the image.
        final HandlerThread handlerThread = new HandlerThread("PixelCopier");
        handlerThread.start();
        // Make the request to copy.


        PixelCopy.request(view, bitmap, (copyResult) -> {
            if (copyResult == PixelCopy.SUCCESS) {
                try {
                   saveBitmapToDisk(bitmap, filename);
                   alertDialog.dismiss();

                   MostrarMensajePantalla = 1;
                   //Toast.makeText(this,"Foto tomada con exito",Toast.LENGTH_SHORT).show();
                   /* MediaScannerConnection.scanFile(this,
                            new String[] { file.toString() }, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> uri=" + uri);
                                }
                            });*/
                } catch (Exception e) {
                    Toast toast = Toast.makeText(RealidadVirtualActivity.this, e.toString(),
                            Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }


               /* Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                        "Photo saved", Snackbar.LENGTH_LONG);
                snackbar.setAction("Open in Photos", v -> {
                    File photoFile = new File(filename);

                    Uri photoURI = FileProvider.getUriForFile(RealidadVirtualActivity.this,
                            RealidadVirtualActivity.this.getPackageName() + ".provider",
                            photoFile);
                    Intent intent = new Intent(Intent.ACTION_VIEW, photoURI);
                    intent.setDataAndType(photoURI, "image/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);

                });
                snackbar.show();*/
            } else {
                Toast toast = Toast.makeText(RealidadVirtualActivity.this,
                        "Failed to copyPixels: " + copyResult, Toast.LENGTH_LONG);
                toast.show();
            }
            handlerThread.quitSafely();
        }, new Handler(handlerThread.getLooper()));



           dialog.show();


    }


    private void saveBitmapToDisk(Bitmap bitmap, String filename) {

        try {

            File imageFile = new File(filename);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();


        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }


    private void  Mensaje(){
        dialog = new Dialog(this,R.style.SlideTheme);
        //deshabilitamos el título por defecto
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //obligamos al usuario a pulsar los botones para cerrarlo
        dialog.setCancelable(false);
        //establecemos el contenido de nuestro dialog
        dialog.setContentView(R.layout.dialog_mensaje);


        btnDialogAceptar = dialog.findViewById(R.id.btn_aceptar_dialog_mensaje);

        btnDialogAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

        /*Rect displayRectangle = new Rect();
        Window window = getWindow();

        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        dialog.getWindow().setLayout((int)(displayRectangle.width() *
                0.9f), ViewGroup.LayoutParams.WRAP_CONTENT);*/


    }


/*

    private void setUpModel() {
        ModelRenderable.builder()
                .setSource(this,
                        RenderableSource.builder().setSource(

                                this,
                                Uri.parse(),
                                RenderableSource.SourceType.GLB)
                                .setScale(0.75f)
                                .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                                .build())

                .setRegistryId(Model_URL)



                .build()
                .thenAccept(renderable -> modelRenderable = renderable)
                .exceptionally(throwable -> {
                    Log.i("Model","cant load");
                    Toast.makeText(RealidadVirtualActivity.this,"Model can't be Loaded", Toast.LENGTH_SHORT).show();
                    return null;
                });
    }

    private void setUpPlane(){
        arFragment.setOnTapArPlaneListener(((hitResult, plane, motionEvent) -> {
            Anchor anchor = hitResult.createAnchor();
            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(arFragment.getArSceneView().getScene());
            createModel(anchorNode);
        }));
    }

    private void createModel(AnchorNode anchorNode){
        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.setParent(anchorNode);
        node.setRenderable(modelRenderable);
        node.select();
    }
*/


}
