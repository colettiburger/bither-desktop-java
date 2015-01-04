package net.bither.xrandom;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import net.bither.Bither;
import net.bither.bitherj.core.Address;
import net.bither.bitherj.crypto.ECKey;
import net.bither.bitherj.crypto.SecureCharSequence;
import net.bither.bitherj.utils.PrivateKeyUtil;
import net.bither.utils.KeyUtil;
import net.bither.utils.PeerUtil;
import net.bither.viewsystem.base.Buttons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UEntropyDialog extends JDialog implements UEntropyCollector
        .UEntropyCollectorListener {
    private JPanel contentPane;
    private JButton buttonCancel;

    private JProgressBar pb;

    private long lastInputTime = System.currentTimeMillis();
    private UEntropyCollector uEntropyCollector;
    private static final int MinGeneratingTime = 5000;
    private int targetCount;

    public UEntropyDialog(int targetCount, SecureCharSequence password) {

        setContentPane(contentPane);

        setModal(true);
        this.targetCount = targetCount;
        Dimension dimension = Bither.getMainFrame().getSize();
        setSize(dimension);
        setMinimumSize(dimension);
        setMaximumSize(dimension);
        Buttons.modifCanelButton(buttonCancel);
        setLocation(Bither.getMainFrame().getLocationOnScreen());
        uEntropyCollector = new UEntropyCollector(this);
        addMouseMotionListener(mouseMotionAdapter);


        // ep.addKeyListener(keyListener);

        addKeyListener(keyListener);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                quit();
            }
        });
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                quit();
            }
        });


        GenerateThread generateThread = new GenerateThread(uEntropyCollector, password);
        generateThread.start();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(3, 1, new Insets(10, 10, 10, 10), -1, -1));
        contentPane.setBackground(new Color(-328966));
        contentPane.setFont(new Font(contentPane.getFont().getName(), contentPane.getFont().getStyle(), 18));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setOpaque(false);
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        pb = new JProgressBar();
        pb.setMaximum(100);
        pb.setStringPainted(true);
        panel1.add(pb, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.setOpaque(false);
        contentPane.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel3.setOpaque(false);
        contentPane.add(panel3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.setOpaque(false);
        panel3.add(panel4, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonCancel = new JButton();
        this.$$$loadButtonText$$$(buttonCancel, ResourceBundle.getBundle("viewer").getString("cancel"));
        panel4.add(buttonCancel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }


    private class GenerateThread extends Thread {
        private double saveProgress = 0.1;
        private double startProgress = 0.01;
        private double progressKeyRate = 0.5;
        private double progressEntryptRate = 0.5;

        private long startGeneratingTime;

        private SecureCharSequence password;
        private Runnable cancelRunnable;

        private UEntropyCollector entropyCollector;

        public GenerateThread(UEntropyCollector entropyCollector, SecureCharSequence password) {
            this.entropyCollector = entropyCollector;
            this.password = password;
        }


        @Override
        public synchronized void start() {
            if (password == null) {
                throw new IllegalStateException("GenerateThread does not have password");
            }
            startGeneratingTime = System.currentTimeMillis();
            super.start();
            onProgress(startProgress);
        }

        public void cancel(Runnable cancelRunnable) {
            this.cancelRunnable = cancelRunnable;
        }

        private void finishGenerate() {
            if (password != null) {
                password.wipe();
                password = null;
            }
            PeerUtil.stopPeer();
            entropyCollector.stop();
        }

        @Override
        public void run() {
            boolean success = false;
            final ArrayList<String> addressStrs = new ArrayList<String>();
            double progress = startProgress;
            double itemProgress = (1.0 - startProgress - saveProgress) / (double) targetCount;

            try {
                entropyCollector.start();
                PeerUtil.stopPeer();

                java.util.List<Address> addressList = new ArrayList<Address>();
                for (int i = 0;
                     i < targetCount;
                     i++) {
                    if (cancelRunnable != null) {
                        finishGenerate();
                        SwingUtilities.invokeLater(cancelRunnable);
                        return;
                    }

                    XRandom xRandom = new XRandom(entropyCollector);
                    ECKey ecKey = ECKey.generateECKey(xRandom);
                    ecKey.setFromXRandom(true);
                    progress += itemProgress * progressKeyRate;
                    onProgress(progress);
                    if (cancelRunnable != null) {
                        finishGenerate();
                        SwingUtilities.invokeLater(cancelRunnable);

                        return;
                    }
                    // start encrypt
                    ecKey = PrivateKeyUtil.encrypt(ecKey, password);
                    Address address = new Address(ecKey.toAddress(), ecKey.getPubKey(),
                            PrivateKeyUtil.getEncryptedString(ecKey), ecKey.isFromXRandom());
                    ecKey.clearPrivateKey();
                    addressList.add(address);
                    addressStrs.add(address.getAddress());

                    progress += itemProgress * progressEntryptRate;
                    onProgress(progress);
                }
                entropyCollector.stop();
                password.wipe();
                password = null;

                if (cancelRunnable != null) {
                    finishGenerate();
                    SwingUtilities.invokeLater(cancelRunnable);

                    return;
                }

                KeyUtil.addAddressListByDesc(addressList);
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            finishGenerate();
            if (success) {
                while (System.currentTimeMillis() - startGeneratingTime < MinGeneratingTime) {

                }
                onProgress(1);
                onSuccess(addressStrs);
            } else {
                onFailed();
            }
        }


        private void onSuccess(final ArrayList<String> addresses) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    quit();
                    Bither.getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    Bither.getCoreController().fireRecreateAllViews(true);
                    Bither.getCoreController().fireDataChangedUpdateNow();
                    if (Bither.getMainFrame() != null) {
                        Bither.getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }

                }
            });
        }

        private void onFailed() {
            quit();
        }

    }

    private void onProgress(final double progress) {
        pb.setValue((int) (progress * 100));

    }

    MouseMotionAdapter mouseMotionAdapter = new MouseMotionAdapter() {
        @Override
        public void mouseMoved(MouseEvent mouseEvent) {
            byte[] timeBytes = SeedUtils.seedTime();
            byte[] clientBytes = SeedUtils.seedInt16(mouseEvent.getX() * mouseEvent.getY());
            byte[] bytes = new byte[timeBytes.length + clientBytes.length];
            System.arraycopy(timeBytes, 0, bytes, 0, timeBytes.length);
            System.arraycopy(clientBytes, 0, bytes, timeBytes.length, clientBytes.length);
            uEntropyCollector.onNewData(bytes, UEntropyCollector.UEntropySource.Mouse);
            lastInputTime = System.currentTimeMillis();
            contentPane.getGraphics().fillOval(mouseEvent.getX(), mouseEvent.getY(), 5, 5);

            //  Circle.drawCircle(mouseEvent.getX(), mouseEvent.getY(), 2, Color.black, contentPane.getGraphics());
//            contentPane.getGraphics().drawRect(mouseEvent.getX(), mouseEvent.getY(), 3, 3);

        }
    };


    KeyListener keyListener = new KeyListener() {

        @Override
        public void keyTyped(KeyEvent keyEvent) {


        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            long timestamp = System.currentTimeMillis();
            byte[] timeBytes = SeedUtils.seedTime();
            byte keyByte = SeedUtils.seedInt8(keyEvent.getKeyCode());
            byte timeByte = SeedUtils.seedInt8(timestamp - lastInputTime);
            byte[] bytes = new byte[timeBytes.length + 2];
            System.arraycopy(timeBytes, 0, bytes, 0, timeBytes.length);
            bytes[timeBytes.length] = keyByte;
            bytes[timeBytes.length + 1] = timeByte;
            lastInputTime = System.currentTimeMillis();
            uEntropyCollector.onNewData(bytes, UEntropyCollector.UEntropySource.Keyboard);
            Dimension dimension = contentPane.getSize();
            int x = (int) ((double) dimension.getWidth() * Math.random());
            int y = (int) ((double) dimension.getHeight() * Math.random());
            contentPane.getGraphics().drawString(String.valueOf(keyEvent.getKeyChar()), x, y);

        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {


        }
    };


    private void quit() {
        setVisible(false);
        dispose();
    }

    @Override
    public void onUEntropySourceError(Exception e, IUEntropySource source) {
        e.printStackTrace();
        if (uEntropyCollector.sources().size() == 0) {
            uEntropyCollector.stop();
        }
    }
}
