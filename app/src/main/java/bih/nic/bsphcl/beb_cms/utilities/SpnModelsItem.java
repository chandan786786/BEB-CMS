package bih.nic.bsphcl.beb_cms.utilities;

public class SpnModelsItem {
    private String mModelName = "";
    private int mModelConstant = 0;

    SpnModelsItem(String modelName, int modelConstant) {
        mModelName = modelName;
        mModelConstant = modelConstant;
    }

    public int getModelConstant() {
        return mModelConstant;
    }

    @Override
    public String toString() {
        return mModelName;
    }
}
