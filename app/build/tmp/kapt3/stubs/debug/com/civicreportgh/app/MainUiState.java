package com.civicreportgh.app;

@kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0003\u0004\u0005\u0006B\t\b\u0004\u00a2\u0006\u0004\b\u0002\u0010\u0003\u0082\u0001\u0003\u0007\b\t\u00a8\u0006\n"}, d2 = {"Lcom/civicreportgh/app/MainUiState;", "", "<init>", "()V", "Idle", "Processing", "Success", "Lcom/civicreportgh/app/MainUiState$Idle;", "Lcom/civicreportgh/app/MainUiState$Processing;", "Lcom/civicreportgh/app/MainUiState$Success;", "app_debug"})
public abstract class MainUiState {
    
    private MainUiState() {
        super();
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003\u00a8\u0006\u0004"}, d2 = {"Lcom/civicreportgh/app/MainUiState$Idle;", "Lcom/civicreportgh/app/MainUiState;", "<init>", "()V", "app_debug"})
    public static final class Idle extends com.civicreportgh.app.MainUiState {
        @org.jetbrains.annotations.NotNull()
        public static final com.civicreportgh.app.MainUiState.Idle INSTANCE = null;
        
        private Idle() {
        }
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003\u00a8\u0006\u0004"}, d2 = {"Lcom/civicreportgh/app/MainUiState$Processing;", "Lcom/civicreportgh/app/MainUiState;", "<init>", "()V", "app_debug"})
    public static final class Processing extends com.civicreportgh.app.MainUiState {
        @org.jetbrains.annotations.NotNull()
        public static final com.civicreportgh.app.MainUiState.Processing INSTANCE = null;
        
        private Processing() {
        }
    }
    
    @kotlin.Metadata(mv = {2, 2, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0004\b\u0006\u0010\u0007J\t\u0010\f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\r\u001a\u00020\u0005H\u00c6\u0003J\u001d\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u00d6\u0003J\t\u0010\u0013\u001a\u00020\u0014H\u00d6\u0001J\t\u0010\u0015\u001a\u00020\u0016H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0017"}, d2 = {"Lcom/civicreportgh/app/MainUiState$Success;", "Lcom/civicreportgh/app/MainUiState;", "uri", "Landroid/net/Uri;", "suggestedCategory", "Lcom/civicreportgh/app/IssueCategory;", "<init>", "(Landroid/net/Uri;Lcom/civicreportgh/app/IssueCategory;)V", "getUri", "()Landroid/net/Uri;", "getSuggestedCategory", "()Lcom/civicreportgh/app/IssueCategory;", "component1", "component2", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"})
    public static final class Success extends com.civicreportgh.app.MainUiState {
        @org.jetbrains.annotations.NotNull()
        private final android.net.Uri uri = null;
        @org.jetbrains.annotations.NotNull()
        private final com.civicreportgh.app.IssueCategory suggestedCategory = null;
        
        public Success(@org.jetbrains.annotations.NotNull()
        android.net.Uri uri, @org.jetbrains.annotations.NotNull()
        com.civicreportgh.app.IssueCategory suggestedCategory) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.net.Uri getUri() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.civicreportgh.app.IssueCategory getSuggestedCategory() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final android.net.Uri component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.civicreportgh.app.IssueCategory component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.civicreportgh.app.MainUiState.Success copy(@org.jetbrains.annotations.NotNull()
        android.net.Uri uri, @org.jetbrains.annotations.NotNull()
        com.civicreportgh.app.IssueCategory suggestedCategory) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
}