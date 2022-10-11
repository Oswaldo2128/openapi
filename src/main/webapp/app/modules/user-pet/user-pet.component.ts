import Component from 'vue-class-component';
import { Inject, Vue } from 'vue-property-decorator';
import UserApiService from '@/shared/service/user-api.service';
import { User } from '@/shared/openapi-client';

@Component
export default class UserPet extends Vue {
  @Inject('userApiService')
  private userApiService: () => UserApiService;
  public user: User = null;
  public mounted(): void {
    if (this.$store.getters.account) {
      console.log(`Logged as ${this.$store.getters.account.login}`);
      this.loadUser(this.$store.getters.account.login);
    } else {
      console.log('No logged');
    }
  }
  private loadUser(username: string) {
    this.user = null;
    this.userApiService()
      .getUserByName(username)
      .then(res => {
        this.user = res.data;
        console.log(this.user);
      })
      .catch(e => console.log(e));
  }
}
