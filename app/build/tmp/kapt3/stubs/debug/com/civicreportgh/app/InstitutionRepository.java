package com.civicreportgh.app;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u00c6\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u000e\u0010\r\u001a\u00020\u00072\u0006\u0010\u000e\u001a\u00020\u0006J\u000e\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\fR\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\b\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0011"}, d2 = {"Lcom/civicreportgh/app/InstitutionRepository;", "", "<init>", "()V", "defaultMapping", "", "Lcom/civicreportgh/app/IssueCategory;", "Lcom/civicreportgh/app/Institution;", "remoteMapping", "updateFromRemote", "", "json", "", "institutionFor", "category", "categoryFromLabel", "label", "app_debug"})
public final class InstitutionRepository {
    @org.jetbrains.annotations.NotNull()
    private static final java.util.Map<com.civicreportgh.app.IssueCategory, com.civicreportgh.app.Institution> defaultMapping = null;
    @org.jetbrains.annotations.Nullable()
    private static java.util.Map<com.civicreportgh.app.IssueCategory, com.civicreportgh.app.Institution> remoteMapping;
    @org.jetbrains.annotations.NotNull()
    public static final com.civicreportgh.app.InstitutionRepository INSTANCE = null;
    
    private InstitutionRepository() {
        super();
    }
    
    public final void updateFromRemote(@org.jetbrains.annotations.NotNull()
    java.lang.String json) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.civicreportgh.app.Institution institutionFor(@org.jetbrains.annotations.NotNull()
    com.civicreportgh.app.IssueCategory category) {
        return null;
    }
    
    /**
     * Very small keyword heuristic that turns an ML Kit image label
     * (e.g. "Road", "Water", "Trash") into one of our issue categories.
     */
    @org.jetbrains.annotations.NotNull()
    public final com.civicreportgh.app.IssueCategory categoryFromLabel(@org.jetbrains.annotations.NotNull()
    java.lang.String label) {
        return null;
    }
}