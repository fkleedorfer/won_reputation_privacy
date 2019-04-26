package msz.User;

import msz.ACL;
import msz.Message.Certificate;
import msz.Message.Reputationtoken;
import msz.Signer.Signer;
import msz.TrustedParty.Params;
import msz.Utils.ECUtils;
import msz.Utils.HashUtils;
import msz.WonProtocol;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;


public class Supplier implements ACL, WonProtocol {

    private Params params;
    private Certificate certificate;
    private String foreignRandomHash;
    private KeyPair keyPair;

    public Supplier() {
        try {
            this.keyPair = ECUtils.generateKeyPair();
        } catch (InvalidAlgorithmParameterException | NoSuchProviderException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public Supplier(Params params) {
        try {
            this.keyPair = ECUtils.generateKeyPair();
        } catch (InvalidAlgorithmParameterException | NoSuchProviderException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        this.params = params;
    }

    public void setup() {

    }

    public void registration() {

    }

    public void preparation() {

    }

    public void validation() {

    }

    public void verifiction() {

    }

    public Certificate registerWithSystem(Signer sp) {
        try {
            this.certificate = sp.registerClient(keyPair.getPublic());
            return this.certificate;
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Certificate registerWithSystem() {
        return null;
    }

    @Override
    public Certificate getCertificate() {
        return this.certificate;
    }

    @Override
    public String createRandomHash() throws NoSuchAlgorithmException {
        return HashUtils.generateRandomHash();
    }

    @Override
    public void exchangeHash(String randomHash) {
        this.foreignRandomHash = randomHash;
    }

    @Override
    public byte[] signHash(String randomHash) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, SignatureException {
        // TODO sign Hash with the publickey of the certificate
        Signature signatureOfRandomHash = Signature.getInstance("SHA256withECDSA");
        signatureOfRandomHash.initSign(this.keyPair.getPrivate());
        signatureOfRandomHash.update(randomHash.getBytes(StandardCharsets.UTF_8));
        return signatureOfRandomHash.sign();
    }

    @Override
    public Reputationtoken createReputationToken(byte[] sigS) {
        // TODO signHash
        // TODO create Reputationtoken with own cert and signature of Hash
        return new Reputationtoken(this.certificate, sigS, new byte[0]);
    }

    @Override
    public boolean verifySignature(byte[] signatureRandomHash, String sr, Certificate cert) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature verifySignature = Signature.getInstance("SHA256withECDSA");
        verifySignature.initVerify(cert.getPublicKey());
        verifySignature.update(sr.getBytes(StandardCharsets.UTF_8));
        return verifySignature.verify(signatureRandomHash);
    }
}
