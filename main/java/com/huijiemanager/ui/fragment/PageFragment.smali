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
    .line 21
    invoke-direct {p0}, Lcom/huijiemanager/base/BaseFragment;-><init>()V

    .line 28
    new-instance v0, Ljava/util/ArrayList;

    invoke-direct {v0}, Ljava/util/ArrayList;-><init>()V

    iput-object v0, p0, Lcom/huijiemanager/ui/fragment/PageFragment;->t:Ljava/util/ArrayList;

    return-void
.end method

.method public static e(Lcom/huijiemanager/ui/fragment/PageFragment;)V
    .registers 1
    .param p0, "page"    # Lcom/huijiemanager/ui/fragment/PageFragment;

    .prologue
    .line 31
    return-void
.end method

.method private getList()Ljava/util/List;
    .registers 2

    .prologue
    .line 65
    const/4 v0, 0x0

    return-object v0
.end method


# virtual methods
.method public a()V
    .registers 1

    .prologue
    .line 33
    return-void
.end method

.method public refreshItem(Lcom/huijiemanager/ui/a/m;)V
    .registers 10
    .param p1, "var1"    # Lcom/huijiemanager/ui/a/m;

    .prologue
    .line 42
    invoke-virtual {p1}, Lcom/huijiemanager/ui/a/m;->a()Ljava/lang/String;

    move-result-object v5

    .line 43
    .local v5, "var4":Ljava/lang/String;
    iget-object v7, p0, Lcom/huijiemanager/ui/fragment/PageFragment;->t:Ljava/util/ArrayList;

    invoke-virtual {v7}, Ljava/util/ArrayList;->iterator()Ljava/util/Iterator;

    move-result-object v3

    .line 45
    .local v3, "var2":Ljava/util/Iterator;
    move-object v0, p0

    .line 46
    .local v0, "a":Lcom/huijiemanager/ui/fragment/PageFragment;
    invoke-direct {p0}, Lcom/huijiemanager/ui/fragment/PageFragment;->getList()Ljava/util/List;

    move-result-object v1

    .line 47
    .local v1, "b":Ljava/util/List;
    move-object v2, v1

    .line 48
    .local v2, "c":Ljava/util/List;
    invoke-static {v0, v2}, Lcom/yess/TestSmali;->RecvicePublicBean(Lcom/huijiemanager/ui/fragment/PageFragment;Ljava/util/List;)V

    .line 50
    :cond_13
    invoke-interface {v3}, Ljava/util/Iterator;->hasNext()Z

    move-result v7

    if-eqz v7, :cond_40

    .line 51
    invoke-interface {v3}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v4

    .line 52
    .local v4, "var3":Ljava/lang/Object;
    instance-of v7, v4, Lcom/huijiemanager/http/response/QuareOrderFiltrateResponse$OrdersBean;

    if-eqz v7, :cond_13

    move-object v6, v4

    .line 53
    check-cast v6, Lcom/huijiemanager/http/response/QuareOrderFiltrateResponse$OrdersBean;

    .line 54
    .local v6, "var5":Lcom/huijiemanager/http/response/QuareOrderFiltrateResponse$OrdersBean;
    invoke-virtual {v6}, Lcom/huijiemanager/http/response/QuareOrderFiltrateResponse$OrdersBean;->getId()I

    move-result v7

    invoke-static {v7}, Ljava/lang/String;->valueOf(I)Ljava/lang/String;

    move-result-object v7

    invoke-virtual {v7, v5}, Ljava/lang/String;->equals(Ljava/lang/Object;)Z

    move-result v7

    if-eqz v7, :cond_13

    .line 55
    const-string v7, "0"

    invoke-virtual {v6, v7}, Lcom/huijiemanager/http/response/QuareOrderFiltrateResponse$OrdersBean;->setCanCollect(Ljava/lang/String;)V

    .line 56
    iget-object v7, p0, Lcom/huijiemanager/ui/fragment/PageFragment;->q:Landroid/support/v7/widget/RecyclerView;

    invoke-virtual {v7}, Landroid/support/v7/widget/RecyclerView;->getAdapter()Landroid/support/v7/widget/RecyclerView$Adapter;

    move-result-object v7

    invoke-virtual {v7}, Landroid/support/v7/widget/RecyclerView$Adapter;->notifyDataSetChanged()V

    .line 62
    .end local v4    # "var3":Ljava/lang/Object;
    .end local v6    # "var5":Lcom/huijiemanager/http/response/QuareOrderFiltrateResponse$OrdersBean;
    :cond_40
    return-void
.end method
