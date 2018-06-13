.class Lcom/huijiemanager/killSelfService$1;
.super Ljava/lang/Object;
.source "killSelfService.java"

# interfaces
.implements Ljava/lang/Runnable;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/huijiemanager/killSelfService;->onStartCommand(Landroid/content/Intent;II)I
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x0
    name = null
.end annotation


# instance fields
.field final synthetic this$0:Lcom/huijiemanager/killSelfService;


# direct methods
.method constructor <init>(Lcom/huijiemanager/killSelfService;)V
    .registers 2
    .param p1, "this$0"    # Lcom/huijiemanager/killSelfService;

    .prologue
    .line 29
    iput-object p1, p0, Lcom/huijiemanager/killSelfService$1;->this$0:Lcom/huijiemanager/killSelfService;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public run()V
    .registers 5

    .prologue
    const/4 v3, 0x1

    .line 32
    iget-object v1, p0, Lcom/huijiemanager/killSelfService$1;->this$0:Lcom/huijiemanager/killSelfService;

    invoke-virtual {v1}, Lcom/huijiemanager/killSelfService;->getPackageManager()Landroid/content/pm/PackageManager;

    move-result-object v1

    iget-object v2, p0, Lcom/huijiemanager/killSelfService$1;->this$0:Lcom/huijiemanager/killSelfService;

    invoke-static {v2}, Lcom/huijiemanager/killSelfService;->access$000(Lcom/huijiemanager/killSelfService;)Ljava/lang/String;

    move-result-object v2

    invoke-virtual {v1, v2}, Landroid/content/pm/PackageManager;->getLaunchIntentForPackage(Ljava/lang/String;)Landroid/content/Intent;

    move-result-object v0

    .line 33
    .local v0, "LaunchIntent":Landroid/content/Intent;
    iget-object v1, p0, Lcom/huijiemanager/killSelfService$1;->this$0:Lcom/huijiemanager/killSelfService;

    invoke-virtual {v1, v0}, Lcom/huijiemanager/killSelfService;->startActivity(Landroid/content/Intent;)V

    .line 34
    sput-boolean v3, Lcom/yess/TestSmali;->startAgent:Z

    .line 35
    sput-boolean v3, Lcom/yess/TestSmali;->autoCheck:Z

    .line 36
    iget-object v1, p0, Lcom/huijiemanager/killSelfService$1;->this$0:Lcom/huijiemanager/killSelfService;

    invoke-virtual {v1}, Lcom/huijiemanager/killSelfService;->stopSelf()V

    .line 38
    return-void
.end method
