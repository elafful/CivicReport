package com.civicreportgh.app;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u0016\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012J\u000e\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\u0012J\u000e\u0010\u0015\u001a\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\u0017J\u0014\u0010\u0018\u001a\u00020\u000e2\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u000e0\u001aJ\u0018\u0010\u001b\u001a\u00020\u000e2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001dH\u0002J\u0006\u0010\u001f\u001a\u00020\u000eJ.\u0010 \u001a\u00020\u00172\u0006\u0010!\u001a\u00020\u00172\u0006\u0010\"\u001a\u00020\u00172\u0006\u0010#\u001a\u00020\u00172\u0006\u0010$\u001a\u00020\u00172\u0006\u0010%\u001a\u00020\u0017J\u0006\u0010&\u001a\u00020\u000eJ6\u0010\'\u001a\u00020(2\u0006\u0010)\u001a\u00020\u00172\u0006\u0010*\u001a\u00020\u00172\u0006\u0010\"\u001a\u00020\u00172\u0006\u0010#\u001a\u00020\u00172\u0006\u0010$\u001a\u00020\u00172\u0006\u0010%\u001a\u00020\u0017R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\b0\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006+"}, d2 = {"Lcom/civicreportgh/app/ReportViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "application", "Landroid/app/Application;", "<init>", "(Landroid/app/Application;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/civicreportgh/app/ReportUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "initReport", "", "uri", "Landroid/net/Uri;", "suggestedCategory", "Lcom/civicreportgh/app/IssueCategory;", "updateCategory", "category", "updateDescription", "description", "", "fetchLocation", "onFailure", "Lkotlin/Function0;", "reverseGeocode", "lat", "", "lng", "submitReport", "formatEmailBody", "intro", "categoryLabel", "descLabel", "locLabel", "footer", "saveReportLocally", "prepareEmailData", "Lcom/civicreportgh/app/EmailReportData;", "subjectTemplate", "bodyIntro", "app_debug"})
public final class ReportViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.civicreportgh.app.ReportUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.civicreportgh.app.ReportUiState> uiState = null;
    
    public ReportViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.civicreportgh.app.ReportUiState> getUiState() {
        return null;
    }
    
    public final void initReport(@org.jetbrains.annotations.NotNull()
    android.net.Uri uri, @org.jetbrains.annotations.NotNull()
    com.civicreportgh.app.IssueCategory suggestedCategory) {
    }
    
    public final void updateCategory(@org.jetbrains.annotations.NotNull()
    com.civicreportgh.app.IssueCategory category) {
    }
    
    public final void updateDescription(@org.jetbrains.annotations.NotNull()
    java.lang.String description) {
    }
    
    public final void fetchLocation(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onFailure) {
    }
    
    private final void reverseGeocode(double lat, double lng) {
    }
    
    public final void submitReport() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatEmailBody(@org.jetbrains.annotations.NotNull()
    java.lang.String intro, @org.jetbrains.annotations.NotNull()
    java.lang.String categoryLabel, @org.jetbrains.annotations.NotNull()
    java.lang.String descLabel, @org.jetbrains.annotations.NotNull()
    java.lang.String locLabel, @org.jetbrains.annotations.NotNull()
    java.lang.String footer) {
        return null;
    }
    
    public final void saveReportLocally() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.civicreportgh.app.EmailReportData prepareEmailData(@org.jetbrains.annotations.NotNull()
    java.lang.String subjectTemplate, @org.jetbrains.annotations.NotNull()
    java.lang.String bodyIntro, @org.jetbrains.annotations.NotNull()
    java.lang.String categoryLabel, @org.jetbrains.annotations.NotNull()
    java.lang.String descLabel, @org.jetbrains.annotations.NotNull()
    java.lang.String locLabel, @org.jetbrains.annotations.NotNull()
    java.lang.String footer) {
        return null;
    }
}