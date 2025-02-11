package msz.bakk.protocol.Utils;

import msz.bakk.protocol.vocabulary.REP;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import won.protocol.util.RdfUtils;
import won.protocol.util.WonRdfUtils;

public class WonRepRdfUtils extends WonRdfUtils {
    private static final Logger logger = LoggerFactory.getLogger(WonRepRdfUtils.class);

    public static Model addStuff(Model model) {
        // Model messageModel = createModelWithBaseResource();
        // Resource baseRes =
        // messageModel.createResource(messageModel.getNsPrefixURI(""));
        // baseRes.addProperty(WONCON.text, message, XSDDatatype.XSDstring);
        // return messageModel;
        return null;
    }

    public static String getRepProperty(Model m, Property p) {
        Statement stmt = m.getProperty(RdfUtils.getBaseResource(m), p);

        if (stmt != null) {
            return stmt.getObject().asLiteral().getLexicalForm();
        }

        return null;
    }

    public static Model createBaseModel() {
        return createModelWithBaseResource();
    }

    private static Model createModelWithBaseResource() {
        Model model = ModelFactory.createDefaultModel();
        model.setNsPrefix("", REP.BASE_URI);
        model.createResource(model.getNsPrefixURI(""));
        return model;
    }
}
