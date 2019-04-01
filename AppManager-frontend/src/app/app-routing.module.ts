import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AppComponent } from './app.component';
import { ApplicationComponent } from './application/application.component';
import { UserComponent } from './user/user.component';
import { NotFoundComponent } from './not-found/not-found.component';

const routes: Routes = [
  {
    path:'apps',
    component: ApplicationComponent
  },
  {
    path:'users',
    component: UserComponent
  },
  {
    path:'',
    component:ApplicationComponent,
    pathMatch:'full'
  },
  {
    path:'++',
    component:NotFoundComponent
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes,{enableTracing:true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
