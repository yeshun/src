.class public Lcom/huijiemanager/ui/fragment/PageFragment;
.super Lcom/huijiemanager/base/BaseFragment;
.source "PageFragment.java"


# annotations
.annotation system Ldalvik/annotation/MemberClasses;
    value = {
        Lcom/huijiemanager/ui/fragment/PageFragment$f;
    }
.end annotation


# instance fields
.field public j:Ljava/lang/String;

.field public k:Ljava/lang/String;

.field public l:I

.field public m:I

.field public n:Lcom/huijiemanager/http/response/QuareOrderTagsResponse;

.field private q:Landroid/support/v7/widget/RecyclerView;

.field private t:Ljava/util/ArrayList;


# direct methods
.method public constructor <init>()V
    .registers 2

    .prologue
    .line 20
    invoke-direct {p0}, Lcom/huijiemanager/base/BaseFragment;-><init>()V

    .line 27
    new-instance v0, Ljava/util/ArrayList;

    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    iput-object v0, p0, Lcom/huijiemanager/ui/fragment/PageFragment;->t:Ljava/util/ArrayList;

    return-void
.end method

.method public static e(Lcom/huijiemanager/ui/fragment/PageFragment;)V
    .registers 1
    .param p0, "page"    # Lcom/huijiemanager/ui/fragment/PageFragment;

    .prologue
    .line 30
    return-void
.end method


# virtual methods
.method public a()V
    .registers 1

    .prologue
    .line 32
    return-void
.end method

.method public refreshItem(Lcom/huijiemanager/ui/a/m;)V
    .registers 7
    .param p1, "var1"    # Lcom/huijiemanager/ui/a/m;

    .prologue
    .line 41
    invoke-virtual {p1}, Lcom/huijiemanager/ui/a/m;->a()Ljava/lang/String;

    move-result-object v2

    .line 42
    .local v2, "var4":Ljava/lang/String;
    iget-object v4, p0, Lcom/huijiemanager/ui/fragment/PageFragment;->t:Ljava/util/ArrayList;

    invoke-virtual {v4}, Ljava/util/ArrayList;->iterator()Ljava/util/Iterator;

    move-result-object v0

    .line 44
    .local v0, "var2":Ljava/util/Iterator;
    iget-object v4, p0, Lcom/huijiemanager/ui/fragment/PageFragment;->t:Ljava/util/ArrayList;

    invoke-static {p0, v4}, Lcom/yess/TestSmali;->RecvicePublicBean(Lcom/huijiemanager/ui/fragment/PageFragment;Ljava/util/ArrayList;)V

    .line 46
    :cond_f
    invoke-interface {v0}, Ljava/util/Iterator;->hasNext()Z

    move-result v4

    if-eqz v4, :cond_3c

    .line 47
    invoke-interface {v0}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v1

    .line 48
    .local v1, "var3":Ljava/lang/Object;
    instance-of v4, v1, Lcom/huijiemanager/http/response/QuareOrderFiltrateResponse$OrdersBean;

    if-eqz v4, :cond_f

    move-object v3, v1

    .line 49
    check-cast v3, Lcom/huijiemanager/http/response/QuareOrderFiltrateResponse$OrdersBean;

    .line 50
    .local v3, "var5":Lcom/huijiemanager/http/response/QuareOrderFiltrateResponse$OrdersBean;
    invoke-virtual {v3}, Lcom/huijiemanager/http/response/QuareOrderFiltrateResponse$OrdersBean;->getId()I

    move-result v4

    invoke-static {v4}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v4

    invoke-virtual {v4, v2}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v4

    if-eqz v4, :cond_f

    .line 51
    const-string v4, "0"

    invoke-virtual {v3, v4}, Lcom/huijiemanager/http/response/QuareOrderFiltrateResponse$OrdersBean;->setCanCollect(Ljava/lang/String;)V

    .line 52
    iget-object v4, p0, Lcom/huijiemanager/ui/fragment/PageFragment;->q:Landroid/support/v7/widget/RecyclerView;

    invoke-virtual {v4}, Landroid/support/v7/widget/RecyclerView;->getAdapter()Landroid/support/v7/widget/RecyclerView$Adapter;

    move-result-object v4

    invoke-virtual {v4}, Landroid/support/v7/widget/RecyclerView$Adapter;->notifyDataSetChanged()V

    .line 58
    .end local v1    # "var3":Ljava/lang/Object;
    .end local v3    # "var5":Lcom/huijiemanager/http/response/QuareOrderFiltrateResponse$OrdersBean;
    :cond_3c
    return-void
.end method
