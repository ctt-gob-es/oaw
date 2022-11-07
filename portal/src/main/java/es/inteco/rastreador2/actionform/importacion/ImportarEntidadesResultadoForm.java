package es.inteco.rastreador2.actionform.importacion;

public class ImportarEntidadesResultadoForm {
	private boolean validImport;
	private int numSegments;
	private int numAdminLevels;
	private int numClassificationLabels;
	private int numLabels;
	private int numComplexities;
	private int numSeedTypes;
	private int numScopes;
	private int numSeeds;

	public boolean isValidImport() {
		return validImport;
	}

	public void setValidImport(boolean validImport) {
		this.validImport = validImport;
	}

	public int getNumSegments() {
		return numSegments;
	}

	public void setNumSegments(int numSegments) {
		this.numSegments = numSegments;
	}

	public int getNumAdminLevels() {
		return numAdminLevels;
	}

	public void setNumAdminLevels(int numAdminLevels) {
		this.numAdminLevels = numAdminLevels;
	}

	public int getNumClassificationLabels() {
		return numClassificationLabels;
	}

	public void setNumClassificationLabels(int numClassificationLabels) {
		this.numClassificationLabels = numClassificationLabels;
	}

	public int getNumLabels() {
		return numLabels;
	}

	public void setNumLabels(int numLabels) {
		this.numLabels = numLabels;
	}

	public int getNumComplexities() {
		return numComplexities;
	}

	public void setNumComplexities(int numComplexities) {
		this.numComplexities = numComplexities;
	}

	public int getNumSeedTypes() {
		return numSeedTypes;
	}

	public void setNumSeedTypes(int numSeedTypes) {
		this.numSeedTypes = numSeedTypes;
	}

	public int getNumScopes() {
		return numScopes;
	}

	public void setNumScopes(int numScopes) {
		this.numScopes = numScopes;
	}

	public int getNumSeeds() {
		return numSeeds;
	}

	public void setNumSeeds(int numSeeds) {
		this.numSeeds = numSeeds;
	}
}
