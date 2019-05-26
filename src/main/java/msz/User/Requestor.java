package msz.User;

import msz.ACL;
import msz.Message.Message;
import msz.Message.Certificate;
import msz.Message.Reputationtoken;
import msz.Signer.Signer;
import msz.Utils.ECUtils;
import msz.TrustedParty.Params;
import msz.Utils.HashUtils;
import msz.Utils.RSAUtils;
import msz.WonProtocol;
import org.bouncycastle.math.ec.ECPoint;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;

/**
 * Requestor is considered as Alice
 */
public class Requestor implements ACL, WonProtocol {
    private final Params params;
    private Certificate certificate;
    private String foreignRandomHash;
    private KeyPair keyPair;
    private Certificate foreignCertificate;


    public Requestor(Params params) {
        try {
            this.keyPair = ECUtils.generateKeyPair();
        } catch (InvalidAlgorithmParameterException | NoSuchProviderException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        this.params = params;
    }

    public Requestor(Params params, ECPoint y, Message m, String[] Attributes) {
        try {
            this.keyPair = ECUtils.generateKeyPair();
        } catch (InvalidAlgorithmParameterException | NoSuchProviderException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        this.params = params;
    }

    /**
     * @source https://files.zotero.net/12620611427/From%20Zero%20Knowledge%20Proofs%20to%20Bulletproofs%20Paper%20.pdf
     */
    private void createCommitment() {
        ECPoint rnd = ECUtils.createRandomPoint(params.getGroup());

        // use h0...hn for the combined Pedersen commitment
        // use z,h0...hn for the blinded Pedersen commitment

        // sum of the commitments h0 - hn is equivalent to commitment of (h0 + ... + hn)
        // C = generator * value + randomPoint * randomness
        // C = (C1 + ... + Cn) * randomness

        // multiply each attribute with the associated ECPoint in params (hs)
        // at the end add randomness * hs0
        ECPoint commitment = rnd.multiply(this.params.getH().getAffineXCoord().toBigInteger())
                .multiply((this.params.getH().getAffineYCoord().toBigInteger()));
        for(ECPoint point : this.params.getHs()) {
//            commitment.add(point.multiply());
        }
    }

    private void createMessageToSign() {

    }

    public void setup() {
        createMessageToSign();
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
            this.certificate = sp.registerClient(this.keyPair.getPublic());
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
        return null;
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
    public byte[] signHash() throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, SignatureException {
        // TODO sign Hash with the publickey of the certificate
        return RSAUtils.signString(this.keyPair, this.foreignRandomHash);
    }

    @Override
    public Reputationtoken createReputationToken(byte[] sigR) {
        // TODO s2ignHash
        // TODO create Reputationtoken with own cert and signature of Hash
        return new Reputationtoken(this.certificate, sigR);
    }

    @Override
    public boolean verifySignature(byte[] signatureRandomHash, String sr, Certificate foreignCertificate) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException {
        boolean isHashCorrect;

        Signature verifySignature = Signature.getInstance("SHA256withECDSA");
        verifySignature.initVerify(foreignCertificate.getPublicKey());
        verifySignature.update(sr.getBytes(StandardCharsets.UTF_8));
        isHashCorrect = verifySignature.verify(signatureRandomHash);

        if(isHashCorrect) {
            this.foreignCertificate = foreignCertificate;
        }

        return isHashCorrect;
    }

    @Override
    public void exchangeReputationToken(byte[] RTs) {

    }

    public Reputationtoken createReputationToken(Certificate certR, byte[] sigR) {
        return new Reputationtoken(certR, sigR);
    }
}
